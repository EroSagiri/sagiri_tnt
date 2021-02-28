package me.sagiri

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.Nullable
import kotlin.math.round

public final class Tnt : JavaPlugin(), Listener {
    private var genTntPlayers : ArrayList<Player> = arrayListOf()

    override fun onEnable() {
        // 注册事件
        server.pluginManager.registerEvents(this, this)
        genTntPlayers.clear()
    }

    override fun onDisable() {
        genTntPlayers.clear()
    }

    // 指令处理
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // tnt 指令
        if(command.name == "tnt") {
            // 通过名字得到指令发送者的玩家对象
            val player = server.getPlayer(sender.name)
            // 指令发送者是否是玩家
            if(player is Player) {
                if (player.hasPermission("some.pointless.permission")) {
                    val targetPlayer : Player = if(args.size == 1) {
                        if(server.getPlayer(args[0]) is Player) {
                            server.getPlayer(args[0])!!
                        } else {
                            sender.sendMessage("玩家 ${args[0]} 不存在或不在线")
                            return false
                        }
                    } else {
                        player
                    }

                    if (targetPlayer in genTntPlayers) {
                        genTntPlayers.remove(targetPlayer)
                        sender.sendMessage("玩家 ${targetPlayer.name} 关闭成功")
                    } else {
                        genTntPlayers.add(targetPlayer)
                        sender.sendMessage("玩家 ${targetPlayer.name} 启用成功")
                    }
                } else {
                    sender.sendMessage("没有权限")
                    return false
                }
            } else {
                if(args.size == 1) {
                    if (sender.hasPermission("some.pointless.permission") ) {
                        if(server.getPlayer(args[0]) is Player) {
                            val targetPlayer = server.getPlayer(args[0])

                            if (targetPlayer in genTntPlayers) {
                                genTntPlayers.remove(targetPlayer)
                                if (targetPlayer != null) {
                                    sender.sendMessage("玩家 ${targetPlayer.name} 关闭成功")
                                }
                            } else {
                                if (targetPlayer != null) {
                                    genTntPlayers.add(targetPlayer)
                                }
                                if (targetPlayer != null) {
                                    sender.sendMessage("玩家 ${targetPlayer.name} 启用成功")
                                }
                            }
                        } else {
                            sender.sendMessage("玩家 ${args[0]} 不存在或不在线")
                            return false
                        }
                    } else {
                        sender.sendMessage("没有权限")
                    }
                } else {
                    sender.sendMessage("没有玩家名")
                    return  false
                }
            }
        }

        return  true
    }

    /**
     * 玩家登陆事件（登陆就死？
     */
    @EventHandler
    fun onLogin(event : PlayerLoginEvent) {
//        val player = event.player
//        player.health = 0.0;

    }

    /**
     * 玩家退出关闭设置
     */
    @EventHandler
    fun onQuit(event : PlayerQuitEvent) {
        val player = event.player
        if(player in genTntPlayers) {
            genTntPlayers.remove(player)
        }
    }

    /**
     * 玩家移动事件
     */
    @EventHandler
    fun onPlayerMove(event : PlayerMoveEvent) {
        val player : Player = event.player
        if(player in genTntPlayers) {
            // 深度
            var h = -1;
            // 广度
            var r = 1;
            // y轴修正,确保在脚底
            val xh = -1;

            val playerLocation = player.location
            val world = playerLocation.world

            val x1 = playerLocation.blockX - r
            val z1 = playerLocation.blockZ - r
            val y1 = playerLocation.blockY + xh

            val x2 = playerLocation.blockX + r
            val z2 = playerLocation.blockZ + r
            var y2 = playerLocation.blockY + xh + h + 1


            for(x in x1 .. x2) {
                for(z in z1 .. z2) {
                    for(y in y2 .. y1) {
                        val block : Block = world.getBlockAt(x, y, z)
                        block.type = Material.TNT
                    }
                }
            }
        }
    }
}