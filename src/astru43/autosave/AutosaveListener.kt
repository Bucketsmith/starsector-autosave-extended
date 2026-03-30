package autosave

import com.fs.starfarer.api.campaign.BaseCampaignEventListener

class AutosaveListener : BaseCampaignEventListener(false) {

    override fun reportGameSaved() {
        AutosaveCooldownManager.markSaved()
    }
}
