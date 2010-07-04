package net.wazari.util.system.wrapper;

import java.util.Arrays;
import net.wazari.common.plugins.Importer;
import net.wazari.common.plugins.ProcessCallback;


public class ImageMagickWrapper implements Importer {
    @Override
    public boolean shrink(ProcessCallback cb, String source, String dest, int width) {
        return 0 == cb.execWaitFor(new String[]{"convert", "-resize", "" + width + "x", source, dest});
    }

    @Override
    public boolean thumbnail(ProcessCallback cb, String source, String dest, int height) {
        return 0 == cb.execWaitFor(new String[]{"convert", "-thumbnail", "x" + height, source, dest});
    }

    @Override
    public boolean rotate(ProcessCallback cb, String degrees, String source, String dest) {
        return 0 == cb.execWaitFor(new String[]{"convert", "-rotate", degrees, source, dest});
    }

    @Override
    public String getName() {
        return "Image Magick wrapper" ;
    }

    @Override
    public String getVersion() {
        return "1" ;
    }

    @Override
    public Capability[] supports() {
        return new Capability[] {Capability.ROTATE, Capability.SHRINK, Capability.THUMBNAIL} ;
    }

    @Override
    public boolean supports(String type, String ext, Capability cap) {
        if ((type != null && type.contains("image")))
            return Arrays.asList(supports()).contains(cap) ;
        else return false ;
    }

    @Override
    public void fullscreen(ProcessCallback cb, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SanityStatus sanityCheck(ProcessCallback cb) {
        if (cb.execWaitFor(new String[]{"convert", "--version"}) == 0){
            return SanityStatus.PASS;
        } else {
            return SanityStatus.FAIL ;
        }
    }

    @Override
    public String getDescription() {
        return "Wrapper for the Image Magick toolbox" ;
    }

    public String getSupportedFilesDesc() {
        return "all kind of photos" ;
    }
}
