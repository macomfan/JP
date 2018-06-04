/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WordSetting;

import JPWord.Data.IWordDictionary;
/**
 *
 * @author u0151316
 */
public class DisplaySetting {

    private static final String DISP_SETTING = "DISP_SETTING";
    private IWordDictionary dict_ = null;
    private boolean displayKanJi_ = false;

    public DisplaySetting(IWordDictionary dict) {
        dict_ = dict;
    }

    public boolean readConfig() {
        String setting = dict_.getSetting().getString(DISP_SETTING);
        if (setting.equals("1")) {
            displayKanJi_ = true;
        } else {
            displayKanJi_ = false;
        }
        return true;
    }

    public void saveConfig() throws Exception {
        if (displayKanJi_) {
            dict_.getSetting().setString(DISP_SETTING, "1");
        } else {
            dict_.getSetting().setString(DISP_SETTING, "0");
        }
    }

    public boolean isDisplayKanJi() {
        return displayKanJi_;
    }

    public void setDisplayKanJi(boolean DisplayKanJi) {
        this.displayKanJi_ = DisplayKanJi;
    }
}
