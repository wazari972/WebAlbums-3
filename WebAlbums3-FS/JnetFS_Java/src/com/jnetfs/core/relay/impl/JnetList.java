/**
 * JNet-fs is based on the work Fuse-J. Copyright (C) 2009 Jacky WU
 * (hongzhi.wu@hotmail.com)
 *
 * This program can be distributed under the terms of the GNU LGPL. See the file
 * COPYING.LIB
 */
package com.jnetfs.core.relay.impl;

import com.jnetfs.core.relay.JnetJNIConnector;

public class JnetList extends JnetOperate {

    public static JnetList instance = new JnetList();

    public JnetList() {
        super("list");
    }

    public static void addName(JnetJNIConnector conn, int pos, String name) {
        conn.setString("file_" + pos, name);
    }

    public static void setCount(JnetJNIConnector conn, int count) {
        conn.setInteger("file_count", count);
    }
}
