package limonblaze.originsclasses.common.registry;

import com.google.common.collect.Sets;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.bientity.DoubleComparingBiEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.block.SimpleBlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SimpleEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.SimpleItemCondition;
import limonblaze.originsclasses.OriginsClasses;
import limonblaze.originsclasses.common.apoli.condition.item.ToolActionCondition;
import limonblaze.originsclasses.util.EntityUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraftforge.common.ToolActions.*;

public class OriginsClassesConditions {

    public static final DeferredRegister<BiEntityCondition<?>> BIENTITY_CONDITIONS = DeferredRegister.create(ApoliRegistries.BIENTITY_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<DoubleComparingBiEntityCondition> YAW_DIFF = BIENTITY_CONDITIONS.register("yaw_diff", () ->
        new DoubleComparingBiEntityCondition(EntityUtils::yawDiff));

    public static final DeferredRegister<EntityCondition<?>> ENTITY_CONDITIONS = DeferredRegister.create(ApoliRegistries.ENTITY_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleEntityCondition> ANIMAL = ENTITY_CONDITIONS.register("animal", () ->
        new SimpleEntityCondition(entity -> entity instanceof Animal));

    public static final DeferredRegister<ItemCondition<?>> ITEM_CONDITIONS = DeferredRegister.create(ApoliRegistries.ITEM_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleItemCondition> MELEE = ITEM_CONDITIONS.register("melee", () ->
        new SimpleItemCondition( stack ->
                (
                        stack.getDescriptionId().equals("item.tetra.modular_single") ||
                        stack.getDescriptionId().equals("item.tetra.modular_double") ||
                        Enchantments.SHARPNESS.canEnchant(stack)
                )));
    public static final RegistryObject<SimpleItemCondition> SWORD = ITEM_CONDITIONS.register("sword", () ->
            new SimpleItemCondition( stack ->
                    (
                            stack.getDescriptionId().equals("tetra:modular_sword") ||
                            Enchantments.SHARPNESS.canEnchant(stack) && !Enchantments.BLOCK_EFFICIENCY.canEnchant(stack)
                    )));
    public static final RegistryObject<SimpleItemCondition> RANGE = ITEM_CONDITIONS.register("range", () ->
        new SimpleItemCondition( stack ->
                (
                        stack.getDescriptionId().equals("item.tetra.modular_bow") ||
                        stack.getDescriptionId().equals("item.tetra.modular_crossbow") ||
                        stack.getItem() instanceof ProjectileWeaponItem
                )));
    public static final RegistryObject<SimpleItemCondition> HOE = ITEM_CONDITIONS.register("hoe", () ->
            new SimpleItemCondition( stack ->
                    (
                            stack.canPerformAction(HOE_DIG)
                   )));
    public static final RegistryObject<SimpleItemCondition> PICKAXE = ITEM_CONDITIONS.register("pickaxe", () ->
            new SimpleItemCondition( stack ->
                    (
                            stack.canPerformAction(PICKAXE_DIG)
                    )));
    public static final RegistryObject<SimpleItemCondition> AXE = ITEM_CONDITIONS.register("axe", () ->
            new SimpleItemCondition( stack ->
                    (
                            stack.canPerformAction(AXE_DIG)
                    )));
    public static final RegistryObject<SimpleItemCondition> SHOVEL = ITEM_CONDITIONS.register("shovel", () ->
            new SimpleItemCondition( stack ->
                    (
                            stack.canPerformAction(SHOVEL_DIG)
                    )));
    public static final RegistryObject<SimpleItemCondition> TOOL = ITEM_CONDITIONS.register("tool", () ->
        new SimpleItemCondition( stack ->
                (
                        stack.getDescriptionId().equals("item.tetra.modular_single") ||
                        stack.getDescriptionId().equals("item.tetra.modular_double") ||
                        stack.getItem() instanceof DiggerItem ||
                        stack.getItem() instanceof ShearsItem
                )));
    public static final RegistryObject<SimpleItemCondition> SHIELD = ITEM_CONDITIONS.register("shield", () ->
        new SimpleItemCondition(stack -> (
                stack.getDescriptionId().equals("item.tetra.modular_shield") ||
                stack.getItem() instanceof ShieldItem
        )));
    public static final RegistryObject<SimpleItemCondition> HELMET = ITEM_CONDITIONS.register("helmet", () ->
        new SimpleItemCondition(stack -> stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.HEAD));
    public static final RegistryObject<SimpleItemCondition> CHESTPLATE = ITEM_CONDITIONS.register("chestplate", () ->
        new SimpleItemCondition(stack -> stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.CHEST));
    public static final RegistryObject<SimpleItemCondition> LEGGINGS = ITEM_CONDITIONS.register("leggings", () ->
        new SimpleItemCondition(stack -> stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.LEGS));
    public static final RegistryObject<SimpleItemCondition> SHOES = ITEM_CONDITIONS.register("shoes", () ->
        new SimpleItemCondition(stack -> stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.FEET));
    public static final RegistryObject<ToolActionCondition> TOOL_ACTION = ITEM_CONDITIONS.register("tool_action", ToolActionCondition::new);

    public static final DeferredRegister<BlockCondition<?>> BLOCK_CONDITIONS = DeferredRegister.create(ApoliRegistries.BLOCK_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleBlockCondition> CROP = BLOCK_CONDITIONS.register("crop", () ->
        new SimpleBlockCondition((wv, bp, sbs) -> {
            BlockState state = sbs.get();
            if(state.getBlock() instanceof CropBlock crop) {
                return crop.isMaxAge(state);
            }
            return false;
        }));

}
