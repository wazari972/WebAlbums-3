/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wazari.service.exchange;

/**
 *
 * @author pk033
 */
public interface Configuration {
    String getImagesPath();

    String getFtpPath();

    String getMiniPath();

    String getRootPath();

    String getBackupPath();

    String getTempPath();

    String getConfigFilePath() ;

    String getPluginsPath();

    boolean hasInternet();

    boolean isReadOnly();

    boolean wantAlightenDb();

    boolean wantStats();

    boolean wantXsl();

    int getAlbumSize();

    int getPhotoSize();

    String getSep();
}
