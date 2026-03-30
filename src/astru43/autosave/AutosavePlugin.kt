package astru43.autosave

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global

@Suppress("unused")
class AutosavePlugin : BaseModPlugin() {
    private lateinit var settings: Settings
    private var saver: Saver? = null

    override fun onApplicationLoad() {
        super.onApplicationLoad()
        settings = Settings()
    }

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)
        saver = Saver(settings)
        Global.getSector().addTransientScript(saver)
        Global.getSector().addTransientListener(saver)
    }

    // ✅ Called after any save (manual or autosave)
    override fun afterGameSave() {
        saver?.afterGameSave()
        AutosaveCooldownManager.markSaved()
    }
}
