# Idiot's Saccharine Totems

Repository for the mod: https://modrinth.com/mod/saccharine-totems

Adds 4 new craftable totems, a Ferrous effect, Ferrous Metal and Ferrous Metal Block.

## Totems

> NOTE: All added totems must be held in one of the hands. Inventory-based totem mods are not supported.

- **Totem of Keeping** — Does NOT prevent death. Stores inventory items on death and lets you recover them after respawn.
- **Totem of Ferrous** — Grants the Ferrous effect for a few seconds and Resistance for about a minute. Ferrous makes you invulnerable to most physical damage except magical damage (potions).
- **Totem of Tenacity** — Slightly aggressive but nerfed variant of the vanilla Totem of Undying.
- **Totem of Perseverance** — Slightly defensive but nerfed variant of the vanilla Totem of Undying.

## Ferrous family of items

- **Ferrous Metal** — Used to craft the Totem Core and the Potion of Ferrous.  
- **Ferrous Metal Block** — Items and entities maintain their horizontal velocity when colliding with the block.
- **Totem Core** — Used to make the totems. Can be imbued with a potion effect using honey and sugar. When used (licked), it grants the imbued potion's effect for a short duration. No usage limit.

Formula used to compute the duration (in ticks) for the imbued effect:
```text
max(1, 12 * (sqrt((duration / 10) + 1) - 1)) * sign(duration - 1) + 1
```