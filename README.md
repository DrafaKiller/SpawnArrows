# Spawn Arrows

A PaperMC plugin for minecraft.

## What is this plugin?

Spawn Arrows is a plugin that allows you to craft and use special arrows that spawn mobs where it hits. For example crafting a Zombie Spawn Arrow by combining an arrow and 8 rotten flesh on a crafting table. Most crafting is based on the mob's drops.

## More Features

#### Player Damage
Spawned mobs won't damage their owners, which makes it a possible tool for pvp using them against other players, strategising where to place the mobs.

#### Arrow Level
Arrows will start at level 1, the level of the arrow is the amount of mobs that it will spawn. Upgrade arrows by combining 2 of the same level.

#### Mob Lifetime
Mobs spawned by the arrow won't live forever, the more level the arrow is the more lifetime the mobs that it spawns will have. *Unless changed in the configuration file.*
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
The recipes to craft the special arrows are simple, most of them co nsist on an arrow and the common drops of the mob. Recipes will show on the knowledge book when crafted.

## How does it work?

#### Customization
The plugin offers a configuration file `config.yml` where almost all aspects of the plugin are available.

Disable the crafting, change the mob lifetime, enable mobs attack the owner, disable the mob lifetime, enable mobs to drop items and xp and even create or change recipes. Almost everything is available in the configuration file.