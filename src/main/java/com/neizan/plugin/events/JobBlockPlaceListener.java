package com.neizan.plugin.events;

import com.neizan.plugin.jobs.JobRewardService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JobBlockPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        // Marcamos bloques colocados por jugadores para no pagar al romperlos
        event.getBlockPlaced().setMetadata(
                JobRewardService.PLACED_METADATA,
                new FixedMetadataValue(JobRewardService.getPlugin(), event.getPlayer().getName())
        );
    }
}
