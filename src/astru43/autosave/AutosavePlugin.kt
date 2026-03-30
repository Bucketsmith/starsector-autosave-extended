package astru43.autosave

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global

@Suppress("unused") // This is the mod's entry point
class AutosavePlugin : BaseModPlugin() {

    private lateinit var settings: Settings
    private var saver: Saver? = null

    override fun onApplicationLoad() {
        super.onApplicationLoad()
        settings = Settings()
    }

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)

        // Initialize the Saver
        saver = Saver(settings)

        // Add Saver as a transient script and listener
        Global.getSector().addTransientScript(saver)
        Global.getSector().addTransientListener(saver)

        // Register AutosaveListener to catch manual/vanilla saves
        Global.getSector().listenerManager.addListener(AutosaveListener())
    }

    override fun afterGameSave() {
        saver?.afterGameSave()
    }
}
