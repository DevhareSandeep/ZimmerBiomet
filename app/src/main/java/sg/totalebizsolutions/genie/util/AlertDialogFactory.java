package sg.totalebizsolutions.genie.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog.Builder;

import sg.totalebizsolutions.genie.R;

public class AlertDialogFactory
{
  /**
   * See buildAlertDialog (int, int , int, int, int,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      int titleResId, int messageResId)
  {
    return buildAlertDialog(context, titleResId, messageResId, null);
  }

  /**
   * See buildAlertDialog (int, int , int, int, int,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      int titleResId, int messageResId, OnClickListener listener)
  {
    return buildAlertDialog(context, titleResId, messageResId, 0, 0,
        listener);
  }

  /**
   * See buildAlertDialog (int, int , int, int, int,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      int titleResId, int messageResId, int positiveButtonResId,
      int negativeButtonResId, OnClickListener listener)
  {
    return buildAlertDialog(context, titleResId, messageResId,
        positiveButtonResId, 0, negativeButtonResId, listener);
  }

  /**
   * Returns an {@link Builder} instance based on the specified values.
   *
   * @param titleResId
   *          the resource ID of the String title
   * @param messageResId
   *          the resource ID of the String message
   * @param positiveButtonResId
   *          the resource ID of the String positive button, optional. "OK" as
   *          default string value
   * @param neutralButtonResId
   *          the resource ID of the String neutral button, optional. Displays
   *          neutral button if specified
   * @param negativeButtonResId
   *          the resource ID of the String negative button, optional. Displays
   *          negative button if specified
   * @param listener
   *          the {@link OnClickListener} instance that will be invoked when
   *          buttons is tapped. Optional
   */
  public static Builder buildAlertDialog (Context context,
      int titleResId, int messageResId, int positiveButtonResId,
      int neutralButtonResId, int negativeButtonResId,
      final OnClickListener listener)
  {
    Resources res = context.getResources();

    String title = titleResId != 0 ? res.getString(titleResId) : null;
    String message = messageResId != 0 ? res.getString(messageResId) : null;

    String positiveButton = positiveButtonResId != 0 ? res
        .getString(positiveButtonResId) : res.getString(android.R.string.ok);

    String neutralButton = neutralButtonResId != 0 ? res
        .getString(neutralButtonResId) : null;

    String negativeButton = negativeButtonResId != 0 ? res
        .getString(negativeButtonResId) : null;

    return buildAlertDialog(context, title, message, positiveButton,
        neutralButton, negativeButton, listener);
  }

  /**
   * See buildAlertDialog (String, String , String, String, String,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      String title, String message)
  {
    return buildAlertDialog(context, title, message, null);
  }

  /**
   * See buildAlertDialog (String, String , String, String, String,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      String title, String message, OnClickListener listener)
  {
    return buildAlertDialog(context, title, message,
        context.getString(android.R.string.ok), null, null, listener);
  }

  /**
   * See buildAlertDialog (String, String , String, String, String,
   * DialogInterface.OnClickListener) method.
   */
  public static Builder buildAlertDialog (Context context,
      String title, String message, String positiveButton,
      String negativeButton, OnClickListener listener)
  {
    return buildAlertDialog(context, title, message, positiveButton, null,
        negativeButton, listener);
  }

  /**
   * Returns an {@link Builder} instance based on the specified values.
   *
   * @param title
   *          the String title of the dialog
   * @param message
   *          the String message of the dialog
   * @param positiveButton
   *          null-able, If not null, This will override the default String
   *          value of the positive button which is "OK"
   * @param neutralButton
   *          null-able, If not null, neutral button will be displayed with the
   *          String value specified
   * @param negativeButton
   *          null-able, If not null, negative button will be displayed with the
   *          String value specified
   * @param listener
   *          the listener that will be invoked when button is clicked.
   */
  public static Builder buildAlertDialog (Context context,
      String title, String message, String positiveButton,
      String neutralButton, String negativeButton,
      final OnClickListener listener)
  {
    Builder builder = new Builder(context, R.style.AppDialogTheme);
    builder.setCancelable(false);

    if (title != null)
    {
      builder.setTitle(title);
    }

    if (message != null)
    {
      builder.setMessage(message);
    }

    OnClickListener onClickListener =
        new OnClickListener()
    {
      @Override
      public void onClick (DialogInterface dialog, int which)
      {
        if (listener != null)
        {
          listener.onClick(dialog, which);
        }
        else
        {
          dialog.dismiss();
        }
      }
    };

    if (positiveButton != null)
    {
      builder.setPositiveButton(positiveButton, onClickListener);
    }

    if (neutralButton != null)
    {
      builder.setNeutralButton(neutralButton, onClickListener);
    }

    if (negativeButton != null)
    {
      builder.setNegativeButton(negativeButton, onClickListener);
    }
    return builder;
  }

  /**
   * Returns an {@link ProgressDialog} instance.
   *
   * @param context
   *   the reference context
   * @param messageResID
   *   the message string resource id
   * @return the dialog instance
   */
  public static ProgressDialog buildProgressDialog (Context context, int messageResID)
  {
    ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppDialogTheme);
    progressDialog.setMessage(context.getString(messageResID));
    progressDialog.setCancelable(false);
    progressDialog.setCanceledOnTouchOutside(false);
    return progressDialog;
  }
}
