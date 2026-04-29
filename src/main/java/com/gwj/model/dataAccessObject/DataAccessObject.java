package com.gwj.model.dataAccessObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gwj.AppConfig;
import com.gwj.model.domain.IEntity;

public class DataAccessObject {

    public List<IEntity> read(IEntity entity) {
        List<IEntity> listEntity = new ArrayList<>();
        Class<?> clazz = entity.getClass();
        Connection conn = ConnectionDB.getInstance().getConnection();

        // 1. Monta o WHERE dinâmico
        String where = buildWhereClause(entity);
        String tableName = AppConfig.TABLE_PREFIX + clazz.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM `" + tableName + "` " + where;
        System.out.println("DataAccessObject: sql = " + sql);

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Instancia a classe alvo
                IEntity instance = (IEntity) clazz.getDeclaredConstructor().newInstance();
                
                // 2. Preenche os dados da tabela atual e SOBE na hierarquia (Recursivo)
                fillEntityRecursively(instance, clazz, rs, conn);
                
                // 3. Processa listas/associações (seu código de ParameterizedType...)
                processAssociations(instance, conn);

                listEntity.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEntity;
    }

    /**
     * MÉTODO RECURSIVO: Preenche a instância com dados da tabela atual
     * e busca dados nas tabelas pai se houver herança.
     */
    private void fillEntityRecursively(IEntity instance, Class<?> currentClazz, ResultSet rs, Connection conn) throws Exception {
        // A. Preenche os atributos da classe atual
        Method[] methods = currentClazz.getDeclaredMethods();
        for (Method method : methods) {
            // System.out.println("DataAccessObject.fillEntityRecursively: method = " + method.getName());
            if (isSetter(method)) {
                String columnName = convertPascalCaseToSnakeCase(method.getName().substring(3));
                try {
                    Object value = rs.getObject(columnName);
                    if (value != null) {
                         // Pega o tipo esperado pelo parâmetro do método (ex: Aluno.setNota(Double d) -> Double.class)
                        Class<?> targetType = method.getParameterTypes()[0];
                        
                        // Converte o valor do RS para o tipo do parâmetro
                        Object convertedValue = convertToTargetType(value, targetType);
                        
                        method.invoke(instance, convertedValue);
                    }
                } catch (SQLException e) {
                    // Coluna não existe nesta tabela
                }
            }
        }

        // B. Condição de parada e Recursão
        Class<?> superClass = currentClazz.getSuperclass();
        if (superClass != null && IEntity.class.isAssignableFrom(superClass) && superClass != Object.class) {
            
            // --- GARANTIA DO ID ---
            // Se o getId() retornar 0 ou null, tentamos pegar o 'id' do ResultSet atual 
            // antes de subir para o pai, caso o ID esteja na tabela da classe filha.
            long currentId = instance.getId();
            if (currentId <= 0) {
                try {
                    currentId = rs.getLong("id");
                    // Tenta achar o setId na hierarquia para garantir que o objeto tenha o ID
                    Method setId = getMethodInHierarchy(instance.getClass(), "setId", long.class);
                    if (setId != null) setId.invoke(instance, currentId);
                } catch (SQLException e) {
                    System.err.println("Erro: Não foi possível localizar o ID para buscar o pai.");
                    return; // Se não tem ID, não tem como buscar o pai
                }
            }

            String parentTable = AppConfig.TABLE_PREFIX + superClass.getSimpleName().toLowerCase();
            String sqlParent = "SELECT * FROM `" + parentTable + "` WHERE id = ?";
            
            try (PreparedStatement stmtP = conn.prepareStatement(sqlParent)) {
                stmtP.setLong(1, currentId); // Usa o ID garantido
                try (ResultSet rsParent = stmtP.executeQuery()) {
                    if (rsParent.next()) {
                        fillEntityRecursively(instance, superClass, rsParent, conn);
                    }
                }
            }
        }
    }
    private Method getMethodInHierarchy(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1 && Modifier.isPublic(method.getModifiers());
    }

    // Métodos convertPascalCaseToSnakeCase e buildWhereClause ...
    // Converter Pascal Case para Snake Case
    private static String convertPascalCaseToSnakeCase(String pascalCase ){
        return pascalCase.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }
    private String buildWhereClause(IEntity entity) {
        StringBuilder where = new StringBuilder();
        Class<?> clazz = entity.getClass();
        // Usamos getMethods para pegar getters da classe atual e das herdadas (ex: nome, sobrenome)
        Method[] methods = clazz.getMethods(); 

        for (Method method : methods) {
            if (isGetter(method)) { // Verifica se é um método get.
                try {
                    Object value = method.invoke(entity);

                    // Só adiciona ao WHERE se o valor não for nulo/vazio/zero
                    if (isValidValue(value)) {
                        System.out.println("DataAccessObject: isValidValue = " + value);
                        // Remove 'get' ou 'is', converte para snake_case
                        // String fieldName = method.getName().startsWith("get") ? 3 : 2;
                        // String columnName = convertPascalCaseToSnakeCase(method.getName().substring(fieldName));

                        String columnName = convertPascalCaseToSnakeCase(method.getName().substring(3));
                        
                        if (where.length() == 0) {
                            where.append(" WHERE ");
                        } else {
                            where.append(" AND ");
                        }

                        // Trata aspas para Strings e formatação para números
                        if (value instanceof String) {
                            where.append("`").append(columnName).append("` LIKE '%").append(value).append("%'");
                        } else {
                            where.append("`").append(columnName).append("` = ").append(value);
                        }
                    }
                } catch (Exception e) {
                    // Log de erro silencioso para métodos que falharem
                }
            }
        }
        return where.toString();
    }

    private boolean isValidValue(Object value) {
    if (value == null) return false;

    // 1. Tratamento para Boolean (Evita que 'false' entre no filtro)
    if (value instanceof Boolean) {
        return (Boolean) value; // Só retorna true se o valor for true
    }

    // 2. Filtra IDs e números (Long, Integer, Double, etc.)
    // Se value for Long, entra aqui e verifica se é > 0
    if (value instanceof Number) {
        return ((Number) value).doubleValue() > 0;
    }

    // 3. Filtra Strings vazias
    if (value instanceof String) {
        return !((String) value).trim().isEmpty();
    }

    // 4. Ignora coleções
    if (value instanceof Collection) {
        return false;
    }

    return true;
}




    /**
     * Define o que é um valor filtrável.
     */
    private boolean isValidFilter(Object value) {
        if (value == null) return false;
        
        if (value instanceof Long) return (Long) value > 0;
        if (value instanceof Integer) return (Integer) value > 0;
        if (value instanceof String) return !((String) value).trim().isEmpty();
        
        return false; // Adicione outros tipos se necessário
    }

    /**
     * Valida se o método é um getter padrão Java Bean.
     */
    private boolean isGetter(Method method) {
        String name = method.getName();
        return (name.startsWith("get") || name.startsWith("is"))
                && method.getParameterCount() == 0
                && !name.equals("getClass")
                && Modifier.isPublic(method.getModifiers());
    }

    // Este método garante que o valor vindo do banco seja compatível com o tipo do método setter. A conversão seja feita antes do invoke.
    private Object convertToTargetType(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;

        // Switch moderno simplifica a lógica de "Para qual tipo vou?"
        return switch (targetType.getSimpleName()) {
            case "Integer", "int" -> switch (value) {
                case Number n -> n.intValue();
                case String s -> Integer.parseInt(s.trim());
                default -> 0;
            };
            case "Long", "long" -> (value instanceof Number n) ? n.longValue() : 0;
            case "BigDecimal" -> new java.math.BigDecimal(value.toString());
            case "Boolean", "boolean" -> switch (value) {
                case Boolean b -> b;
                case Number n -> n.intValue() != 0;
                case String s -> s.equalsIgnoreCase("true") || s.equals("1");
                default -> false;
            };
            case "Date" -> (value instanceof java.util.Date d) ? new java.util.Date(d.getTime()) : 0;
            case "LocalDateTime" -> switch (value) {
                case java.sql.Timestamp t -> t.toLocalDateTime();
                case java.sql.Date d -> d.toLocalDate().atStartOfDay();
                case String s -> java.time.LocalDateTime.parse(s);
                default -> 0;
            };
            default -> 0;
        };
    }

    private void processAssociations(IEntity instance, Connection conn) throws Exception {


        Field[] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            // 1. Verificar se o atributo é uma List
            if (Collection.class.isAssignableFrom(field.getType())) { // Verifida se se trata de coleções.
                
                // Pegar o tipo genérico da lista (ex: Endereco)
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> genericClass = (Class<?>) listType.getActualTypeArguments()[0];

                // Verificar se o tipo da lista implementa IEntity
                if (IEntity.class.isAssignableFrom(genericClass)) { // Verifica se é uma das minhas classes de domínio (ignora as classes padrão do Java).
                    
                    // 2. Instanciar a entidade da lista (ex: new Endereco())
                    IEntity associationEntity = (IEntity) genericClass.getDeclaredConstructor().newInstance();

                    // 3. Setar o ID da classe principal na entidade filha (FK)
                    // Ex: se a classe principal é Cliente, busca setClienteId(Long id)
                    String fkSetterName = "set" + instance.getClass().getSimpleName() + "Id";
                    System.out.println("DataAccessObject: (fkSetterName) = " + fkSetterName + " = " + instance.getId());
                    try {
                        // Busca o método na classe filha e invoca
                        Method fkSetter = getMethodInHierarchy(genericClass, fkSetterName, Long.class);
                        System.out.println("DataAccessObject: genericClass = " + genericClass.getSimpleName() + " associationEntity = " + associationEntity.getClass().getSimpleName());
                        if (fkSetter != null) { // Verifica se encontrou o método (não nulo).
                            System.out.println("DataAccessObject: ...processando fkSetter.invoke ");
                            fkSetter.invoke(associationEntity, instance.getId());
                        }

                        // 4. Chama o read recursivamente e atribui o resultado à lista
                        // Nota: 'this.read' retorna List<IEntity>, fazemos o cast para a coleção do campo
                        List<IEntity> result = this.read(associationEntity);
                        
                        field.setAccessible(true);
                        field.set(instance, result);

                    } catch (Exception e) {
                        System.err.println("Aviso: Não foi possível processar associação " + field.getName());
                    }
                }
            
            } else if (IEntity.class.isAssignableFrom(field.getType()) && !Collection.class.isAssignableFrom(field.getType())) { // Filtro: Identifica campos que implementam IEntity, mas não são coleções.
                
                Class<?> fieldClass = field.getType();
                
                // 1. Instanciar a entidade (ex: new Wishlist())
                IEntity associationEntity = (IEntity) fieldClass.getDeclaredConstructor().newInstance();
                System.out.println("DataAccessObject: associationEntity.getClass().getSimpleName() = " + associationEntity.getClass().getSimpleName());

                // 2. Setar o ID da classe principal (FK)
                String fkSetterName = "set" + instance.getClass().getSimpleName() + "Id";
                Method fkSetter = getMethodInHierarchy(fieldClass, fkSetterName, Long.class);
                
                if (fkSetter != null) {
                    fkSetter.invoke(associationEntity, instance.getId());
                    System.out.println("DataAccessObject: instance.getId() = " + instance.getId());
                    
                    // 3. Chamar o read e pegar apenas o primeiro resultado
                    List<IEntity> results = this.read(associationEntity);
                    
                    if (!results.isEmpty()) {
                        field.setAccessible(true);
                        field.set(instance, results.get(0)); // Pega a primeira e única
                    }
                }
            }
        }
    }
}
