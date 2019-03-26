package TransitSystemExceptions;

public class CriticalFileMissingException extends Exception {

  public CriticalFileMissingException(){
    super();
  }
  public CriticalFileMissingException(String msg){
    super(msg);
  }
}
