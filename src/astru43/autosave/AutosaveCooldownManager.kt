package astru43.autosave

import lunalib.lunaSettings

object AutosaveCooldownManager {

    private var lastAutosaveTime: Long = 0
    private val minIntervalMinutes: Int
        get() = lunaSettings.getInt("autosave_min_interval", 15) // Default 15 min

    fun canAutosave(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastAutosaveTime >= minIntervalMinutes * 60_000L) {
            lastAutosaveTime = now
            return true
        }
        return false
    }
}
