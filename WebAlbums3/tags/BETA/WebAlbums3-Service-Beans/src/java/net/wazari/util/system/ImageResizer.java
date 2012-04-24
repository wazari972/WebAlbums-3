 package net.wazari.util.system;

import java.util.logging.Level;
import net.wazari.common.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Stack;

import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import net.wazari.service.exchange.Configuration;

@Stateless
public class ImageResizer {

    private static final Logger log = Logger.getLogger(ImageResizer.class.toString());
    private static final int HEIGHT = 200;
    @EJB
    private SystemTools sysTool;

    @Asynchronous
    public void resize(Configuration conf, Stack<Element> stack, File author) {
        log.info("Starting the ImageResizer Thread");

        Element current;
        while (!stack.empty()) {
            log.info("Looping");

            current = stack.pop();
            log.info(current.path);

            log.info("Resizing...");
            try {
                if (!thumbnail(current, conf)) {
                    continue;
                }

                log.info("Moving...");
                if (!move(current, conf)) {
                    continue;
                }

                log.info("Done !");
            } catch (URISyntaxException e) {
                log.log(Level.INFO, "URISyntaxException {0}", e);
            } catch (MalformedURLException e) {
                log.log(Level.INFO, "MalformedURLException {0}", e);
            } catch (IOException e) {
                log.log(Level.INFO, "IOExceptionLException {0}", e);
            }

        }
        if (author != null && author.isDirectory()) {
            log.log(Level.INFO, "Nettoyage du dossier {0}", author);
            File[] lst = author.listFiles();

            //supprimer recursivement tous les dossiers de ce repertoire
            for (File f : lst) {
                clean(f);
            }
        } else {
            log.info("Pas de dossier à nettoyer");
        }
        log.info("Finished !");
    }

    public static void clean(File rep) {
        if (rep.isFile()) {
            if ("Thumbs.db".equals(rep.getName())) {
                rep.delete();
            }
            //on fait rien

            log.log(Level.WARNING, "Fichier trouv\u00e9 {0} !", rep);
        } else if (rep.isDirectory()) {
            log.log(Level.INFO, "Suppression du dossier {0} ...", rep);
            File[] lst = rep.listFiles();

            //supprimer recursivement tous les dossiers vides de ce repertoire
            for (File f : lst) {
                clean(f);
            }
            //et supprimer le repertoire lui meme
            rep.delete();
        }
    }

    private static boolean move(Element elt, Configuration conf) throws MalformedURLException, URISyntaxException {
        String url = "file://" + conf.getImagesPath() + conf.getSep() + elt.path;
        log.log(Level.INFO, "SOURCE = {0}", url);
        URI uri = new URL(StringUtil.escapeURL(url)).toURI();
        File destination = new File(uri);
        destination.getParentFile().mkdirs();
        log.log(Level.INFO, "Move {0} to {1}", new Object[]{elt.image, destination});

        if (!elt.image.renameTo(destination)) {
            log.info("Impossible de déplacer ...");
            return false;
        }

        return true;
    }

    private boolean thumbnail(Element source, Configuration conf) throws URISyntaxException, IOException {
        String path = conf.getMiniPath() + conf.getSep() + source.path + ".png";

        File destination = new File(path);
        File parent = destination.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            log.log(Level.WARNING, "Impossible de creer le dossier destination ({0})", parent);
            return false;
        } else {
            log.log(Level.WARNING, "Repertoires parents cr\u00e9es ({0})", parent);
            String ext = null;
            int idx = source.image.getName().lastIndexOf('.');
            if (idx != -1) {
                ext = source.image.getName().substring(idx + 1);
            }
            return sysTool.thumbnail(source.type, ext, source.image.getAbsolutePath(),
                    destination.getAbsolutePath(),
                    HEIGHT);
        }
    }

    public static class Element {

        public String path;
        public File image;
        public String type;

        public Element(String path, File image, String type) {
            this.path = path;
            this.image = image;
            this.type = type;
        }
    }
}