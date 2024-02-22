package breswii.attackmissed;


import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


@Mod("attackmissed")
public class AttackMissed {
    private static final Logger LOGGER = LogManager.getLogger();
    private Random random = new Random();
    public static Properties config;
    public boolean op=false;
    private Map<Entity, Float> heal = new HashMap<>();
    public AttackMissed() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
        config = ConfigManager.loadConfig();
        LOGGER.info("AttackMissed loaded");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingHurt(LivingAttackEvent event) {

        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (target != null&&source != null) {

            String entityName = target.getType().getDescriptionId().toLowerCase();
            String[] parts = entityName.split("\\.");
            String mobName = parts[parts.length - 1];

            if(config.containsKey(mobName) && source instanceof Player ) {
                double mobMissProbability = Double.parseDouble(config.getProperty(mobName));
                op = random.nextDouble() - mobMissProbability < 0;
                if(op) {
                    Player player = (Player) source;
                    event.setCanceled(true);

                    if(heal.get(target)!=null){
                        if(heal.get(target)==target.getHealth()){
                            EntityplaySound(target);
                            sendTitleMessage(player, I18n.get("attackmissed.show"));
                            heal.put(target,target.getHealth());
                        }
                        else{
                            heal.put(target,target.getHealth());
                            return;
                        }
                    }
                    else{
                        heal.put(target,target.getHealth());
                    }
                }
                else {
                    heal.put(target,target.getHealth());
                    op=false;
                }
            }
            else{
                op=false;
            }
        }
        else{
            op=false;
        }
    }


    private void EntityplaySound(Entity entity) {
        entity.level().playSound(
                entity,
                entity.blockPosition(),
                SoundEvents.SHIELD_BLOCK,
                SoundSource.AMBIENT,
                1.0f,
                1.0f
        );
    }

    public static void sendTitleMessage(Player player, String title) {
        player.displayClientMessage(Component.literal(title),true);
    }
}
