package com.google.android.apps.nexuslauncher.smartspace;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.b;
import com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.c;
import com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.e;
import com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.i;
import com.google.android.apps.nexuslauncher.utils.ColorManipulation;

public class SmartspaceCard {

    private final b dI;
    private final long gsaUpdateTIme;
    private final int gsaVersion;
    private final boolean dL;
    private final boolean dM;
    private final long published;
    private final Context mContext;
    private Bitmap mIcon;
    private final Intent mIntent;

    public SmartspaceCard(final Context context, final b di, final Intent mIntent, final boolean dm,
            final Bitmap mIcon, final boolean dl, final long dn, final long dj, final int dk) {
        this.mContext = context.getApplicationContext();
        this.dI = di;
        this.dM = dm;
        this.mIntent = mIntent;
        this.mIcon = mIcon;
        this.published = dn;
        this.gsaUpdateTIme = dj;
        this.gsaVersion = dk;
        this.dL = dl;
    }

    static SmartspaceCard createNew(Context context, i protobufData, boolean z) {
        if (protobufData != null) {
            try {
                Intent parseUri = TextUtils.isEmpty(protobufData.de.cG.cZ) ?
                        null :
                        Intent.parseUri(protobufData.de.cG.cZ, 0);

                Bitmap bitmap = protobufData.dd == null ?
                        null :
                        BitmapFactory
                                .decodeByteArray(protobufData.dd, 0, protobufData.dd.length, null);

                if (bitmap != null) {
                    // TODO: implement this
//                    bitmap = ShadowGenerator.getInstance(context).recreateIcon(
//                            bitmap,
//                            false,
//                            new BlurMaskFilter(
//                                    (float) Utilities.pxFromDp(3.0f, context.getResources().getDisplayMetrics()),
//                                    BlurMaskFilter.Blur.NORMAL),
//                            20,
//                            55);
                }

                return new SmartspaceCard(context, protobufData.de, parseUri, z, bitmap,
                        protobufData.dc, protobufData.df,
                        protobufData.dh, protobufData.dg);
            } catch (Throwable e) {
                Log.e("SmartspaceCard", "from proto", e);
            }
        }

        return null;
    }

    private String cE(final e eVar) {
        final Resources res = mContext.getResources();
        int minutesToEvent = cJ(eVar);
        if (minutesToEvent >= 60) {
            int hours = minutesToEvent / 60;
            int minutes = minutesToEvent % 60;
            String hoursString = res.getQuantityString(R.plurals.smartspace_hours, hours, hours);
            if (minutes <= 0) {
                return hoursString;
            }
            String minutesString = res
                    .getQuantityString(R.plurals.smartspace_minutes, minutes, minutes);
            return res.getString(R.string.smartspace_hours_mins, hoursString, minutesString);
        }
        return res.getQuantityString(R.plurals.smartspace_minutes, minutesToEvent, minutesToEvent);
    }

    private com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.d cG(
            final boolean b) {
        final c ch = this.cH();
        if (ch != null) {
            com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.d d;
            if (b) {
                d = ch.cL;
            } else {
                d = ch.cM;
            }
            return d;
        }
        return null;
    }

    private c cH() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long cd = this.dI.cD;
        final long n = this.dI.cD + this.dI.cE;
        if (currentTimeMillis < cd && this.dI.cB != null) {
            return this.dI.cB;
        }
        if (currentTimeMillis > n && this.dI.cH != null) {
            return this.dI.cH;
        }
        if (this.dI.cC != null) {
            return this.dI.cC;
        }
        return null;
    }

    private int cJ(final e e) {
        return (int) Math.ceil(this.cI(e) / 60000.0);
    }

    private String[] cK(final e[] array, final String s) {
        int i;
        String[] array2;
        String cr;
        for (i = 0, array2 = new String[array.length]; i < array2.length; ++i) {
            switch (array[i].cQ) {
                default: {
                    array2[i] = "";
                    break;
                }
                case 3: {
                    if (s != null && array[i].cS != 0) {
                        array2[i] = s;
                        break;
                    }
                    if (array[i].cR != null) {
                        cr = array[i].cR;
                    } else {
                        cr = "";
                    }
                    array2[i] = cr;
                    break;
                }
                case 1:
                case 2: {
                    array2[i] = this.cE(array[i]);
                    break;
                }
            }
        }
        return array2;
    }

    private boolean cL(
            final com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.d d) {
        boolean b = false;
        if (d != null && d.cN != null && d.cO != null && d.cO.length > 0) {
            b = true;
        }
        return b;
    }

    private String cN(final boolean b) {
        return this.cO(b, null);
    }

    private String cO(final boolean b, final String s) {
        final com.google.android.apps.nexuslauncher.smartspace.nano.SmartspaceProto.d cg = this
                .cG(b);
        if (cg == null || cg.cN == null) {
            return "";
        }
        String cn = cg.cN;
        if (this.cL(cg)) {
            return String.format(cn, (Object[]) this.cK(cg.cO, s));
        }
        if (cn == null) {
            cn = "";
        }
        return cn;
    }

    private static Bitmap cP(final Bitmap bitmap, final int n) {
        final Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(n, PorterDuff.Mode.SRC_IN));
        final Bitmap bitmap2 = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(bitmap2).drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmap2;
    }

    static i cQ(final Context context, final NewCardInfo a) {
        if (a == null) {
            return null;
        }
        final i i = new i();
        final Bitmap ci = a.getBitmap(context);
        Bitmap cp;
        if (ci != null && i.dc) {
            if (a.forWeather) {
                cp = cP(ci, -1);
            } else {
                cp = ci;
            }
        } else {
            cp = ci;
        }
        byte[] flattenBitmap;
        if (cp != null) {
            flattenBitmap = Utilities.flattenBitmap(cp);
        } else {
            flattenBitmap = new byte[0];
        }
        i.dd = flattenBitmap;
        i.dc = (cp != null && new ColorManipulation().dB(cp));
        i.de = a.di;
        i.df = a.dl;
        if (a.dk != null) {
            i.dg = a.dk.versionCode;
            i.dh = a.dk.lastUpdateTime;
        }
        return i;
    }

    public String cA(final boolean b) {
        return this.cO(b, "");
    }

    public String cB(final boolean b) {
        int i = 0;
        final e[] co = this.cG(b).cO;
        if (co != null) {
            while (i < co.length) {
                if (co[i].cS != 0) {
                    return co[i].cR;
                }
                ++i;
            }
        }
        return "";
    }

    public String cC(final String s) {
        return this.cO(true, s);
    }

    public long expires() {
        return this.dI.cF.da;
    }

    long cI(final e e) {
        long cd;
        if (e.cQ == 2) {
            cd = this.dI.cD + this.dI.cE;
        } else {
            cd = this.dI.cD;
        }
        return Math.abs(System.currentTimeMillis() - cd);
    }

    public boolean cM() {
        return System.currentTimeMillis() > this.expires();
    }

    void click(View view) {
        Log.e("SmartspaceCard", "Smartspace has been liberated!" + this);
    }

    public boolean cv() {
        final c ch = this.cH();
        return ch != null && (this.cL(ch.cL) || this.cL(ch.cM));
    }

    public long cw() {
        final c ch = this.cH();
        if (ch != null && this.cL(ch.cL)) {
            final e[] co = ch.cL.cO;
            for (int i = 0; i < co.length; ++i) {
                final e e = co[i];
                if (e.cQ == 1 || e.cQ == 2) {
                    return this.cI(e);
                }
            }
        }
        return 0L;
    }

    public TextUtils.TruncateAt cx(final boolean b) {
        final c ch = this.cH();
        if (ch != null) {
            int n = 0;
            if (b && ch.cL != null) {
                n = ch.cL.cP;
            } else if (!b && ch.cM != null) {
                n = ch.cM.cP;
            }
            switch (n) {
                case 1: {
                    return TextUtils.TruncateAt.START;
                }
                case 2: {
                    return TextUtils.TruncateAt.MIDDLE;
                }
            }
        }
        return TextUtils.TruncateAt.END;
    }

    public String cy() {
        return this.cN(false);
    }

    public boolean cz() {
        return this.dL;
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public String getTitle() {
        return this.cN(true);
    }

    public String toString() {
        return "title:" + this.getTitle() + " expires:" + this.expires() + " published:"
                + this.published
                + " gsaVersion:" + this.gsaVersion + " gsaUpdateTime: " + this.gsaUpdateTIme;
    }
}
