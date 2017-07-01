package sg.totalebizsolutions.genie.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Sandeep Devhare @APAR on 4/3/2017.
 */
//copy raw resources into external device.
public class Setup implements Runnable{
    private Context m_context;
    private File m_sdcard;
    private String m_configdir = "/Genie";

    public Setup(Context context){
        m_context = context;
        m_sdcard = Environment.getExternalStorageDirectory();
    }
    @Override
    public void run() {
        File cfgdir = new File(m_sdcard+m_configdir);
        if(!cfgdir.exists()){
            cfgdir.mkdirs();
        }

      /*  copyResources(R.raw.signatureglenoidguidesystemsetupandplanningbrochure);
        copyResources(R.raw.signaturepersonalizedpatientcareforcompandreverseproductbrochure);
        copyResources(R.raw.signaturepersonalizedpatientcareforcompandreversesurgicaltechnique);*/
    }
    public void copyResources(int resId){
        Log.i("Test", "Setup::copyResources");
        InputStream in = m_context.getResources().openRawResource(resId);
        String filename = m_context.getResources().getResourceEntryName(resId);

        File f = new File(filename);

        if(!f.exists()){
            try {
                OutputStream out = new FileOutputStream(new File(m_sdcard+m_configdir, filename));
                byte[] buffer = new byte[1024];
                int len;
                while((len = in.read(buffer, 0, buffer.length)) != -1){
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                Log.i("Test", "Setup::copyResources - "+e.getMessage());
            } catch (IOException e) {
                Log.i("Test", "Setup::copyResources - "+e.getMessage());
            }
        }
    }



}
