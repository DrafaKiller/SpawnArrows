# Spawn Arrows

A PaperMC plugin for minecraft.

## What is this plugin?

Spawn Arrows is a plugin that allows you to craft and use special arrows that spawn mobs where it hits. For example crafting a Zombie Spawn Arrow by combining an arrow and 8 rotten flesh on a crafting table. Most crafting is based on the drops of the mob.

<p align="center">
   <img src="https://user-images.githubusercontent.com/42767829/112081563-c2c71f80-8b7b-11eb-83cc-bbbc222b303c.png">
</p>

## Download
PaperMC 1.16.5 - [spawnarrows-1.16.5.jar](https://github.com/DrafaKiller/SpawnArrows/releases/download/papermc-1.16.5/spawnarrows-1.16.5.jar)

## More Features

#### Player Damage
Spawned mobs won't damage their owners, making it a possible tool for pvp to use against other players, strategising where to shot them.

#### Arrow Level
Arrows will start at level 1, the level of the arrow is the amount of mobs that it will spawn. Upgrade arrows by combining 2 of the same level.

#### Mob Lifetime
Mobs spawned won't stay forever, the more level the arrow is the more lifetime the mobs will have.
<table>
    <tr>
        <th>Level</th>
        <td>1</td>
        <td>2</td>
        <td>3</td>
        <td>4</td>
        <td>5</td>
        <td>6</td>
        <td>7</td>
        <td>8</td>
        <td>9</td>
        <td>10</td>
    </tr>
    <tr>
        <th>Lifetime</th>
        <td>10s</td>
        <td>15s</td>
        <td>25s</td>
        <td>40s</td>
        <td>60s</td>
        <td>75s</td>
        <td>100s</td>
        <td>150s</td>
        <td>240s</td>
        <td>320s</td>
    </tr>
</table>

#### Mob drops
When a mob that was spawned by a special arrow dies, it won't drop items nor xp. This is to prevent item farming, like multiplication, converting an item into other or converting items into xp.

#### Recipes
The recipes to craft the special arrows are simple, most of them consist on an arrow combined with the common drops of the mob. The recipe is shapeless, which means the order of the items don't matter. Recipes will show on the knowledge book when crafted. [List of all default recipes.](https://github.com/DrafaKiller/SpawnArrows/wiki/Recipes)

## How does it work?

#### Customization
The plugin offers a configuration file [config.yml](https://github.com/DrafaKiller/SpawnArrows/blob/master/src/main/resources/config.yml) where almost all aspects of the plugin are available.

Disable the crafting, change the mob lifetime, enable mobs to attack the owner, disable the mob lifetime, enable mobs to drop items and xp and even create or change recipes. Almost everything is available in the configuration file.