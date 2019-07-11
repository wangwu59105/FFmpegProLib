package com.victor.ffmpeg;

import java.util.ArrayList;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CmdList.java
 * Author: Victor
 * Date: 2019/7/4 17:40
 * Description:
 * -----------------------------------------------------------------
 */
public class CmdList extends ArrayList<String> {
    public CmdList append(String s) {
        this.add(s);
        return this;
    }

    public CmdList append(int i) {
        this.add(i + "");
        return this;
    }

    public CmdList append(float f) {
        this.add(f + "");
        return this;
    }

    public CmdList append(StringBuilder sb) {
        this.add(sb.toString());
        return this;
    }

    public CmdList append(String[] ss) {
        for (String s:ss) {
            if(!s.replace(" ","").equals("")) {
                this.add(s);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : this) {
            sb.append(" ").append(s);
        }
        return sb.toString();
    }
}
