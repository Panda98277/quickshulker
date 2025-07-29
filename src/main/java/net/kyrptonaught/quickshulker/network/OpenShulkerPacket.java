package net.kyrptonaught.quickshulker.network;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenShulkerPacket(int invSlot) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, OpenShulkerPacket> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.invSlot), buf -> new OpenShulkerPacket(buf.readInt()));

    public static final Id<OpenShulkerPacket> ID = new Id<>(Identifier.of(QuickShulkerMod.MOD_ID, "open_shulker_packet"));

    public static void registerReceivePacket() {
        PayloadTypeRegistry.playC2S().register(OpenShulkerPacket.ID, OpenShulkerPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenShulkerPacket.ID, OpenShulkerPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenShulkerPacket.ID, (payload, context) -> context.player().getServer().execute(() -> Util.openItem(context.player(), payload.invSlot)));
    }

    @Environment(EnvType.CLIENT)
    public static void sendOpenPacket(int invSlot) {
        ClientPlayNetworking.send(new OpenShulkerPacket(invSlot));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}