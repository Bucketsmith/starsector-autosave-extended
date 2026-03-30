package astru43.autosave

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.combat.EngagementResultAPI
import org.apache.log4j.Logger
import org.lazywizard.lazylib.campaign.MessageUtils
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Saver(private val settings: Settings) : BaseCampaignEventListener(false), EveryFrameScript {

    enum class SaveType {
        FULL_SAVE,
        AUTOSAVE
    }

    private val logger: Logger = Global.getLogger(Saver::class.java)
    private var shouldSave = false
    private var lastSave: Float = 0f
    private var lastMessage: Float = 0f
    private var saveType: SaveType? = null

    override fun isDone(): Boolean = false
    override fun runWhilePaused(): Boolean = true

    override fun advance(amount: Float) {
        if (settings.useFullSave) lastSave += amount
        else lastSave = 0f

        val ui = Global.getSector().campaignUI
        if (ui.isShowingMenu || ui.isShowingDialog) return

        if (settings.useFullSave) {
            val savePeriodSeconds = toSeconds(settings.fullSavePeriod)
            if (savePeriodSeconds <= lastSave) {
                logger.info("Run full save")
                saveType = SaveType.FULL_SAVE
                ui.cmdSave()
                shouldSave = false
                return
            }

            if (savePeriodSeconds - lastSave <= 4) {
                if (lastMessage >= 1) {
                    MessageUtils.showMessage("Full save in ${(savePeriodSeconds - lastSave).roundToInt()}")
                    lastMessage = 0f
                } else lastMessage += amount
            }
        }

        if (shouldSave && AutosaveCooldownManager.canAutosave()) {
            logger.info("Run autosave")
            saveType = SaveType.AUTOSAVE
            ui.autosave()
            shouldSave = false
        }
    }

    fun afterGameSave() {
        if (saveType == SaveType.FULL_SAVE)
            lastSave = 0f
        saveType = null
    }

    private fun toSeconds(value: Int): Long {
        return TimeUnit.MINUTES.toSeconds(value.toLong())
    }

    override fun reportFleetJumped(
        fleet: CampaignFleetAPI?,
        from: SectorEntityToken?,
        to: JumpPointAPI.JumpDestination?
    ) {
        if (!settings.autosaveOnJump) return
        if (fleet == null) return
        if (fleet.isPlayerFleet && from != to?.destination) {
            logger.info("AUTOSAVE: Jump triggered")
            shouldSave = true
        }
    }

    override fun reportPlayerMarketTransaction(transaction: PlayerMarketTransaction?) {
        if (!settings.autosaveOnTransaction) return
        logger.info("AUTOSAVE: Transaction triggered")
        shouldSave = true
    }

    override fun reportPlayerEngagement(result: EngagementResultAPI?) {
        if (!settings.autosaveOnBattle) return
        logger.info("AUTOSAVE: Battle result triggered")
        shouldSave = true
    }
}
