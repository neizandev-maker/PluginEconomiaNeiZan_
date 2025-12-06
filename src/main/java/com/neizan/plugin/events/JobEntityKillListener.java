package com.neizan.plugin.events;

import com.neizan.plugin.Main;
import com.neizan.plugin.jobs.JobManager;
import com.neizan.plugin.jobs.JobsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class JobEntityKillListener implements Listener {

    private final JobManager jobManager;

    public JobEntityKillListener(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return; // Si no hay jugador que mató, salir
        if (!jobManager.hasJob(killer.getUniqueId())) return;

        JobsEnum job = jobManager.getJob(killer.getUniqueId()).getJobType();

        if (job == JobsEnum.ASESINO) {
            double reward = 10.0; // recompensa ejemplo
            jobManager.getJob(killer.getUniqueId()).addBalance(reward);
            Main.getInstance().getEconomyManager().addBalance(killer.getUniqueId(), reward);
            killer.sendMessage("Has ganado $" + reward + " por tu trabajo de " + job.getNombre());
        }
    }
}
