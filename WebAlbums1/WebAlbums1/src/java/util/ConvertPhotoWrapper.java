package util ;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.* ;

public class ConvertPhotoWrapper implements ImageUtil {
  public static final Logger log = Logger.getLogger("ConvertWrapper");
  static {
    log.setLevel(Level.ALL);
    try {
      log.addAppender(new FileAppender(new SimpleLayout(),"/tmp/ConverPhotoWrapper.log"));
      log.fatal("====================\n\n");

    } catch (Exception e) {}
  }
    public boolean support (String type) {
	return (type != null && type.contains("image"));
    }

  public boolean thumbnail (int height, String source, String dest) {
    try {
      String[] command = new String[5] ;
      command[0] = "convert" ;
      command[1] = "-thumbnail" ;
      command[2] = "x"+height ;
      command[3] = source ;
      command[4] = dest  ;
      log.info(command);
      
      Process ps = Runtime.getRuntime().exec(command);
      
      BufferedReader r = new BufferedReader (new InputStreamReader (ps.getInputStream()));
      boolean finished = false ;
      while (!finished) {
	try {
	  
	  String s ;
	  while ((s = r.readLine()) != null) {
	    log.info(s) ;
	  }
	  r = new BufferedReader (new InputStreamReader (ps.getErrorStream()));
	  while ((s = r.readLine()) != null) {
	    log.info("err - "+s) ;
	  }
	  int ret ;
	  ret = ps.exitValue() ;
	  log.info("returned "+ret);
	  finished = true ;
	  
	} catch (Exception e) {	}
      }
      return true ;
    } catch (Exception e) {
      log.info("Exception thrown ... "+e);
      return false ;
    }
  }

  public boolean rotate (String degrees, String source, String dest) {
    try {
      String[] command = new String[5] ;
      command[0] = "convert" ;
      command[1] = "-rotate" ;
      command[2] = degrees ;
      command[3] = source ;
      command[4] = dest  ;
      //log.info(command);
      
      Process ps = Runtime.getRuntime().exec(command);
      
      BufferedReader r = new BufferedReader (new InputStreamReader (ps.getInputStream()));
      boolean finished = false ;
      while (!finished) {
	try {
	  
	  String s ;
	  while ((s = r.readLine()) != null) {
	    log.info(s) ;
	  }
	  r = new BufferedReader (new InputStreamReader (ps.getErrorStream()));
	  while ((s = r.readLine()) != null) {
	    log.info("err - "+s) ;
	  }
	  int ret ;
	  ret = ps.exitValue() ;
	  log.info("returned "+ret);
	  finished = true ;
	  
	} catch (Exception e) {	}
      }
      return (ps.exitValue() == 0) ;
    } catch (Exception e) {
      log.info("Exception thrown ... "+e);
      return false ;
    }
  }
}