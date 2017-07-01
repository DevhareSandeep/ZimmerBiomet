package sg.totalebizsolutions.genie.util;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Sandeep Devhare @APAR on 4/4/2017.
 */

public class VideoViewCustom extends VideoView {

    private int mForceHeight = 0;
    private int mForceWidth = 0;
    public VideoViewCustom(Context context) {
        super(context);
    }

    public VideoViewCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setVideoURI(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this.getContext(), uri);
        mForceWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        mForceHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        super.setVideoURI(uri);
        super.setVideoURI(uri);
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;

    }
    public void setVideoSize(int width, int height) {
        this.mForceWidth  = width;
        this.mForceHeight = height;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.i("@@@", "onMeasure");
        int width = getDefaultSize(mForceWidth, widthMeasureSpec);
        int height = getDefaultSize(mForceHeight, heightMeasureSpec);
        if (mForceWidth > 0 && mForceHeight > 0) {
            if (mForceWidth * height > width * mForceHeight) {
                // Log.i("@@@", "image too tall, correcting");
                height = width * mForceHeight / mForceWidth;
            } else if (mForceWidth * height < width * mForceHeight) {
                // Log.i("@@@", "image too wide, correcting");
                width = height * mForceWidth / mForceHeight;
            } else {
                // Log.i("@@@", "aspect ratio is correct: " +
                // width+"/"+height+"="+
                // mVideoWidth+"/"+mVideoHeight);
            }
        }
        // Log.i("@@@", "setting size: " + width + 'x' + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getHolder().setSizeFromLayout();
    }
}