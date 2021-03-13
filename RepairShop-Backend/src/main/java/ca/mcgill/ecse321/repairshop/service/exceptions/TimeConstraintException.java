package ca.mcgill.ecse321.repairshop.service.exceptions;

public class TimeConstraintException extends RuntimeException{
    public TimeConstraintException(String msg) {
        super(msg);
    }
}
