package dev.wnuke.nukestack.player;

import com.google.gson.annotations.SerializedName;
import dev.wnuke.nukestack.NukeStack;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PlayerData {
    @SerializedName("u")
    private UUID uuid;
    @SerializedName("ts")
    private long tokens = NukeStack.startingMoney;
    @SerializedName("tt")
    private long lifeTimeTPs = 0;
    @SerializedName("td")
    private long lifeTimeDupes = 0;
    @SerializedName("nn")
    private String nickName = "";
    @SerializedName("ks")
    private long killStreak = 0;
    @SerializedName("ig")
    private ArrayList<UUID> ignored = new ArrayList<>();
    @SerializedName("ll")
    private LastLocation lastLocation;
    @SerializedName("g")
    private String group = "default";

    public PlayerData(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer != null) {
            this.lastLocation = LastLocation.fromLocation(onlinePlayer.getLocation());
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "uuid=" + uuid +
                ", tokens=" + tokens +
                ", lifeTimeTPs=" + lifeTimeTPs +
                ", lifeTimeDupes=" + lifeTimeDupes +
                ", nickName='" + nickName + '\'' +
                ", killStreak=" + killStreak +
                ", ignored=" + ignored +
                ", lastLocation=" + lastLocation.toString() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, getTokens(), getLifeTimeTPs(), getLifeTimeDupes(), getNickName(), killStreak, ignored, lastLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerData)) return false;
        PlayerData that = (PlayerData) o;
        return this.uuid == that.uuid;
    }

    public long getTokens() {
        if (NukeStack.currency) {
            return this.tokens;
        } else {
            return Long.MAX_VALUE;
        }
    }

    public boolean hasIgnored(UUID player) {
        if (ignored == null) ignored = new ArrayList<>();
        return ignored.contains(player);
    }

    public long getStreak() {
        return this.killStreak;
    }

    public long getLifeTimeTPs() {
        return this.lifeTimeTPs;
    }

    public long getLifeTimeDupes() {
        return this.lifeTimeDupes;
    }

    public String getNickName() {
        return this.nickName;
    }

    public PlayerData setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public PlayerData setUuidIfNull(UUID uuid) {
        if (uuid == null) {
            this.uuid = uuid;
        }
        return this;
    }

    public PlayerData addTokens(long amount) {
        if (NukeStack.currency) {
            if (amount < 0) {
                amount *= -1;
            }
            this.tokens += amount;
        }
        return this;
    }

    public PlayerData removeTokens(long amount) {
        if (NukeStack.currency) {
            if (amount < 0) {
                amount *= -1;
            }
            this.tokens -= amount;
        }
        return this;
    }

    public PlayerData toggleIgnore(UUID player) {
        if (ignored == null) ignored = new ArrayList<>();
        if (ignored.contains(player)) ignored.remove(player);
        else ignored.add(player);
        return this;
    }

    public PlayerData incrementStreak() {
        killStreak++;
        return this;
    }

    public PlayerData endStreak() {
        killStreak = 0;
        return this;
    }

    public PlayerData setLogoutLocation(LastLocation lastLocation) {
        this.lastLocation = lastLocation;
        return this;
    }

    public Location getLogoutLocation(Server server) {
        if (lastLocation == null) return null;
        return lastLocation.asLocation(server);
    }

    public PlayerData save() {
        PlayerDataUtilities.savePlayerData(this);
        return this;
    }

    public PlayerData increaseLifeTimeTPs() {
        this.lifeTimeTPs++;
        return this;
    }

    public PlayerData increaseLifeTimeDupes() {
        this.lifeTimeDupes++;
        return this;
    }
}