package com.marketinginapp.startup.ms.user.utils.enums;

public class ConverterEnum {
    public static ERole toERole(String value){
        return switch (value) {
            case "admin" -> ERole.ADMIN;
            case "driver" -> ERole.DRIVER;
            case "moderator" -> ERole.MODERATOR;
            default -> ERole.USER;
        };
    }
    public static EStatus toEStatus(String value){
        return switch (value) {
            case "active" -> EStatus.ACTIVE;
            case "cancel" -> EStatus.CANCEL;
            default -> EStatus.UNAVAILABLE;
        };
    }
    public static EType toEType(String value){
        return switch (value) {
            case "private" -> EType.PRIVATE;
            case "student" -> EType.STUDENT;
            default -> EType.PUBLIC;
        };
    }
}
