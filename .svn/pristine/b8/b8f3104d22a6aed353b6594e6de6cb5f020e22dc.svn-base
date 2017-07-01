package sg.totalebizsolutions.foundation.util;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Defines several utility methods that to query the phone's state and process
 * phone number-related checks.
 */
public final class PhoneUtils
{
  /**
   * Returns true if both phone number strings are well formed and are the same,
   * otherwise, false.
   */
  public static final boolean arePhoneNumbersTheSame (String number1,
    String number2)
  {
    PhoneNumberUtil util = PhoneNumberUtil.getInstance();
    PhoneNumberUtil.MatchType matchType = util.isNumberMatch(number1, number2);
    return   matchType == PhoneNumberUtil.MatchType.NSN_MATCH
          || matchType == PhoneNumberUtil.MatchType.EXACT_MATCH;
  }

  /**
   * Returns the ISO country code equivalent of the current registered
   * operator's MCC (Mobile Country Code) base on a given Context instance.
   */
  public static String getPhoneCountryCode (Context context)
  {
    TelephonyManager manager =
      (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    String countryCode = manager.getSimCountryIso();
    if (countryCode == null || countryCode.length() == 0)
    {
      countryCode = Locale.getDefault().getCountry();
    }
    return countryCode.toUpperCase(Locale.ENGLISH);
  }

  /**
   * Format to makes sure there's a + in front of a TOA_International number.
   */
  public static String formatToTOAInternational (String number)
  {
    return PhoneNumberUtils.stringFromStringAndTOA(number,
      PhoneNumberUtils.TOA_International);
  }

  /**
   * Returns ITU-T telephone/calling code based on a given Context instance.
   */
  public static String getPhoneCallingCode (Context context)
  {
    return CountryCallingCodeMap.get(getPhoneCountryCode(context));
  }

  /**
   * Maps the country codes to actual telephone/calling codes.
   */
  private static Map<String, String> CountryCallingCodeMap;
  static
  {
    Map<String, String> map = new HashMap<>();
    CountryCallingCodeMap = map;
    map.put("AF", "93");
    map.put("AL", "355");
    map.put("DZ", "213");
    map.put("AD", "376");
    map.put("AO", "244");
    map.put("AQ", "672");
    map.put("AR", "54");
    map.put("AM", "374");
    map.put("AW", "297");
    map.put("AU", "61");
    map.put("AT", "43");
    map.put("AZ", "994");
    map.put("BH", "973");
    map.put("BD", "880");
    map.put("BY", "375");
    map.put("BE", "32");
    map.put("BZ", "501");
    map.put("BJ", "229");
    map.put("BT", "975");
    map.put("BO", "591");
    map.put("BA", "387");
    map.put("BW", "267");
    map.put("BR", "55");
    map.put("BN", "673");
    map.put("BG", "359");
    map.put("BF", "226");
    map.put("MM", "95");
    map.put("BI", "257");
    map.put("KH", "855");
    map.put("CM", "237");
    map.put("CA", "1");
    map.put("CV", "238");
    map.put("CF", "236");
    map.put("TD", "235");
    map.put("CL", "56");
    map.put("CN", "86");
    map.put("CX", "61");
    map.put("CC", "61");
    map.put("CO", "57");
    map.put("KM", "269");
    map.put("CG", "242");
    map.put("CD", "243");
    map.put("CK", "682");
    map.put("CR", "506");
    map.put("HR", "385");
    map.put("CU", "53");
    map.put("CY", "357");
    map.put("CZ", "420");
    map.put("DK", "45");
    map.put("DJ", "253");
    map.put("TL", "670");
    map.put("EC", "593");
    map.put("EG", "20");
    map.put("SV", "503");
    map.put("GQ", "240");
    map.put("ER", "291");
    map.put("EE", "372");
    map.put("ET", "251");
    map.put("FK", "500");
    map.put("FO", "298");
    map.put("FJ", "679");
    map.put("FI", "358");
    map.put("FR", "33");
    map.put("PF", "689");
    map.put("GA", "241");
    map.put("GM", "220");
    map.put("GE", "995");
    map.put("DE", "49");
    map.put("GH", "233");
    map.put("GI", "350");
    map.put("GR", "30");
    map.put("GL", "299");
    map.put("GT", "502");
    map.put("GN", "224");
    map.put("GW", "245");
    map.put("GY", "592");
    map.put("HT", "509");
    map.put("HN", "504");
    map.put("HK", "852");
    map.put("HU", "36");
    map.put("IN", "91");
    map.put("ID", "62");
    map.put("IR", "98");
    map.put("IQ", "964");
    map.put("IE", "353");
    map.put("IM", "44");
    map.put("IL", "972");
    map.put("IT", "39");
    map.put("CI", "225");
    map.put("JP", "81");
    map.put("JO", "962");
    map.put("KZ", "7");
    map.put("KE", "254");
    map.put("KI", "686");
    map.put("KW", "965");
    map.put("KG", "996");
    map.put("LA", "856");
    map.put("LV", "371");
    map.put("LB", "961");
    map.put("LS", "266");
    map.put("LR", "231");
    map.put("LY", "218");
    map.put("LI", "423");
    map.put("LT", "370");
    map.put("LU", "352");
    map.put("MO", "853");
    map.put("MK", "389");
    map.put("MG", "261");
    map.put("MW", "265");
    map.put("MY", "60");
    map.put("MV", "960");
    map.put("ML", "223");
    map.put("MT", "356");
    map.put("MH", "692");
    map.put("MR", "222");
    map.put("MU", "230");
    map.put("YT", "262");
    map.put("MX", "52");
    map.put("FM", "691");
    map.put("MD", "373");
    map.put("MC", "377");
    map.put("MN", "976");
    map.put("ME", "382");
    map.put("MA", "212");
    map.put("MZ", "258");
    map.put("NA", "264");
    map.put("NR", "674");
    map.put("NP", "977");
    map.put("NL", "31");
    map.put("AN", "599");
    map.put("NC", "687");
    map.put("NZ", "64");
    map.put("NI", "505");
    map.put("NE", "227");
    map.put("NG", "234");
    map.put("NU", "683");
    map.put("KP", "850");
    map.put("NO", "47");
    map.put("OM", "968");
    map.put("PK", "92");
    map.put("PW", "680");
    map.put("PA", "507");
    map.put("PG", "675");
    map.put("PY", "595");
    map.put("PE", "51");
    map.put("PH", "63");
    map.put("PN", "870");
    map.put("PL", "48");
    map.put("PT", "351");
    map.put("PR", "1");
    map.put("QA", "974");
    map.put("RO", "40");
    map.put("RU", "7");
    map.put("RW", "250");
    map.put("BL", "590");
    map.put("WS", "685");
    map.put("SM", "378");
    map.put("ST", "239");
    map.put("SA", "966");
    map.put("SN", "221");
    map.put("RS", "381");
    map.put("SC", "248");
    map.put("SL", "232");
    map.put("SG", "65");
    map.put("SK", "421");
    map.put("SI", "386");
    map.put("SB", "677");
    map.put("SO", "252");
    map.put("ZA", "27");
    map.put("KR", "82");
    map.put("ES", "34");
    map.put("LK", "94");
    map.put("SH", "290");
    map.put("PM", "508");
    map.put("SD", "249");
    map.put("SR", "597");
    map.put("SZ", "268");
    map.put("SE", "46");
    map.put("CH", "41");
    map.put("SY", "963");
    map.put("TW", "886");
    map.put("TJ", "992");
    map.put("TZ", "255");
    map.put("TH", "66");
    map.put("TG", "228");
    map.put("TK", "690");
    map.put("TO", "676");
    map.put("TN", "216");
    map.put("TR", "90");
    map.put("TM", "993");
    map.put("TV", "688");
    map.put("AE", "971");
    map.put("UG", "256");
    map.put("GB", "44");
    map.put("UA", "380");
    map.put("UY", "598");
    map.put("US", "1");
    map.put("UZ", "998");
    map.put("VU", "678");
    map.put("VA", "39");
    map.put("VE", "58");
    map.put("VN", "84");
    map.put("WF", "681");
    map.put("YE", "967");
    map.put("ZM", "260");
    map.put("ZW", "263");
  }
}
