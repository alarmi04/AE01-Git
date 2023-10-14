package paquet;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroExtension implements FilenameFilter {
	   String extension;
	    FiltroExtension(String extension){
	        this.extension=extension;
	    }
	    
	    /**
	     * @return Devuelve true si el nombre del archivo termina con la extension especificada.
	     */
	    public boolean accept(File dir, String name){
	        return name.endsWith(extension);
	    }
}
