package sg.totalebizsolutions.genie.util;

/**
 * Created by Sandeep Devhare @APAR on 5/17/2017.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import sg.totalebizsolutions.genie.R;

public class CcMediaController extends MediaController {

    ImageButton mCCBtn;
    Context mContext;
    AlertDialog mLangDialog;

    public CcMediaController(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        LayoutParams frameParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT|Gravity.TOP;

        View v = makeCCView();
        addView(v, frameParams);

    }

    private View makeCCView() {
        mCCBtn = new ImageButton(mContext);
        mCCBtn.setImageResource(R.drawable.arrow_right);

        mCCBtn.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                Builder builder = new Builder(mContext);
                builder.setSingleChoiceItems(R.array.langs_Array, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Save Preference and Dismiss the Dialog here
                        Toast.makeText(mContext, "Which ::: "+which, Toast.LENGTH_LONG).show();
                    }

                });
                mLangDialog = builder.create();
                mLangDialog.show();
            }
        });

        return mCCBtn;
    }

}