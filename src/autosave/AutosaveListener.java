package autosave;

import com.fs.starfarer.api.campaign.BaseCampaignEventListener;

public class AutosaveListener extends BaseCampaignEventListener {

    public AutosaveListener() {
        super(false);
    }

    @Override
    public void reportGameSaved() {
        AutosaveCooldownManager.markSaved();
    }
}
