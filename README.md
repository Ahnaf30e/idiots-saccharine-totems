# Idiot's Saccharine Totems

Repository of the mod https://modrinth.com/project/Wr9bRGhm

Adds 4 new craftable totems. Adds a new effect called Ferrous and also Ferrous Metal and Block.

## Totems

> NOTE: All added totems need to be held in one of the hands. Does not support inventory totem mods, yet.

- **Totem of Keeping -** Totem which DOES NOT prevent death. But instead, stores the inventory items on death and lets you use it to get them back after respawn.
- **Totem of Ferrous -** Gives a ferrous effect for a few seconds and Resistance for almost a minute on use. Ferrous makes you invulnarable to almost all physical damages except magical ones (Potions).
- **Totem of Tenacity -** Slightly aggresive but nerfed version of the normal Totem of Undying.
- **Totem of Perseverance -** Close but slightly defensive compared to the normal Totem of Undying, still nerfed.

## Ferrous family of items

- **Ferrous Metal -** Used to craft the totem core and the Potion of Ferrous.
- **Ferrous Metal Block -** Items and entities maintain their horizontal velocity when in collision with the block. (Slightly unbalanced... but fun :)
- **Totem Core -** Used to make the totems. Can be imbued with a potion effect using honey and sugar. When licked, gives the effect of the imbued potion for a bit. No limit on the number of times you can lick (might need to balance this :< )

Formula used to get the duration (in ticks) for the imbued effect ```max(1, 12 * (sqrt((duration / 10) + 1) - 1)) * sign(duration - 1) + 1;```
