package by.unit.bsu.scsm.domain.types;

public enum UserType {
    UNKNOWN(0, "Неизвестный тип"),
    SERVER(1, "Сервер"),
    USER(2, "Пользователь");
    
    private final Integer id;
    private final String name;
    
    private UserType(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    public Integer getId()
    {
        return this.id;
    }
    
    public static UserType forId(final Integer id)
    {
        switch(id) {
            case 0: 
                return UNKNOWN;
            case 1:
                return SERVER;
            case 2:
                return USER;
            default:
                return UNKNOWN;
        }
        //throw new IllegalArgumentException(id.toString() + " is unknown type");
    }
    
    public String getName()
    {
        return this.name;
    }
}
