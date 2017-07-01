package sg.totalebizsolutions.genie.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Sandeep Devhare @APAR on 5/25/2017.
 */


public class MasterDataPojo {

    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("Format")
    @Expose
    private String format;
    @SerializedName("Level1")
    @Expose
    private String level1;
    @SerializedName("Level2")
    @Expose
    private String level2;
    @SerializedName("Level3")
    @Expose
    private String level3;
    @SerializedName("Level4")
    @Expose
    private String level4;
    @SerializedName("Level5")
    @Expose
    private String level5;
    @SerializedName("Level6")
    @Expose
    private String level6;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getLevel3() {
        return level3;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public String getLevel4() {
        return level4;
    }

    public void setLevel4(String level4) {
        this.level4 = level4;
    }

    public String getLevel5() {
        return level5;
    }

    public void setLevel5(String level5) {
        this.level5 = level5;
    }

    public String getLevel6() {
        return level6;
    }

    public void setLevel6(String level6) {
        this.level6 = level6;
    }

}