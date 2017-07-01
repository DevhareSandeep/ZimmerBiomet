package sg.totalebizsolutions.genie.views.categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.core.file.FileConstants;

public class CategoryMetaData
{
  public static final List<String> SELECTED_CATEGORIES = new ArrayList<>();

  private static final Map<String, Integer> COLOR_MAP = new HashMap<>();

  public static final String[] CATEGORIES = new String[]
  {
      FileConstants.eCAT_BRANDS,
      FileConstants.eCAT_MARCOM,
      FileConstants.eCAT_CATALOGUE,
      FileConstants.eCAT_TRAINING,
      FileConstants.eCAT_PRODUCTS,
      FileConstants.sCAT_SURGON
  };

  static
  {
    COLOR_MAP.put(FileConstants.EXTREMITIES, R.color.extremities);
    COLOR_MAP.put(FileConstants.SUBCHONDROPLASTY, R.color.subchin);
    COLOR_MAP.put(FileConstants.SPORTSMED, R.color.sportsmed);
  }

  public static int colorResIDForCategory (String category)
  {
    int colorResID = R.color.black;
    if (COLOR_MAP.containsKey(category))
    {
      colorResID = COLOR_MAP.get(category);
    }
    return colorResID;
  }
}
