package sg.totalebizsolutions.genie.core.file;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FileConstants {
    public static final int ID_EXTREMITIES = 0;
    public static final int ID_SUBCHIN = 1;
    public static final int ID_SPORTSMED = 2;

    public static final String EXTREMITIES = "Extremities";
    public static final String SPORTSMED = "Sports Medicine";
    public static final String SUBCHONDROPLASTY = "Subchondroplasty";

    //Category depends on media index file changes
    //Extrimities
    public static final String eCAT_BRANDS = "Brand";
    public static final String eCAT_MARCOM = "Marketing";
    public static final String eCAT_TRAINING = "Training";
    public static final String eCAT_PRODUCTS = "Anatomy";
    public static final String eCAT_CATALOGUE = "Ordering";
    //Subchondroplasty
    public static final String sCAT_PRODUCTS = "Anatomy";
    public static final String sCAT_CATALOGUE = "Ordering";
    public static final String sCAT_TRAINING = "Training";
    public static final String sCAT_MARCOM = "Marketing";
    public static final String sCAT_SURGON = "Tips&Tricks";
    //Sportsmed
    public static final String spCAT_BRANDS = "Brand";
    public static final String spCAT_MARCOM = "Marketing";
    public static final String spCAT_CATALOGUE = "Ordering";
    public static final String spCAT_TRAINING = "Training";
    public static final String spCAT_PRODUCTS = "Anatomy";

    public static final String FORMAT_CATEGORY = "CATEGORY";
    public static final String FORMAT_PDF = "PDF";
    public static final String FORMAT_MP4 = "MP4";
    public static final String FORMAT_MPG = "MPG";
    public static final String FORMAT_DOCS = "DOCX";
    public static final String FORMAT_XLS = "XLSX";
    public static final String FORMAT_PPT = "PPT";

    @Retention(RetentionPolicy.SOURCE)//Annotations are to be discarded by the compiler.
    @StringDef({FORMAT_CATEGORY, FORMAT_PDF, FORMAT_DOCS, FORMAT_XLS, FORMAT_PPT, FORMAT_MP4, FORMAT_MPG})
    public @interface Format {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FileConstants.ID_EXTREMITIES, FileConstants.ID_SUBCHIN, FileConstants.ID_SPORTSMED})
    public @interface RootFolderID {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({eCAT_BRANDS, eCAT_CATALOGUE, eCAT_MARCOM, eCAT_PRODUCTS, eCAT_TRAINING})
    public @interface Categories {
    }
}
