package astru43.autosave

import lunalib.lunaSettings.LunaSettings

object AutosaveCooldownManager {

    private var lastAutosaveTime: Long = 0

    // Get the minimum interval in minutes from LunaLib, default 15
    private val minIntervalMinutes: Int
        get() = LunaSettings.getInt("autosave_extended", "autosave_min_interval") ?: 15

    // Returns true if enough real time has passed to allow an autosave
    fun canAutosave(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastAutosaveTime >= minIntervalMinutes * 60_000L) {
            lastAutosaveTime = now
            return true
        }
        return false
    }

    // Resets the cooldown timer, call this after any save (manual or autosave)
    fun markSaved() {
        lastAutosaveTime = System.currentTimeMillis()
    }
}
