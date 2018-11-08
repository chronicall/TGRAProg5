## TGRA Programming Assignment 5
### Super Sario - 3D Platformer

#### The Game
This Is Not Mario 64 is the latest in 3D platforming. This elegant and well polished game is a marvel of the future.

But in all seriousness. It's a "platformer" where the goal is the glide around as Bowsette's crown and collect rings, not stars, so _obviously_ this is not Mario 64!

If you manage to collect all the rings, gaining one point for each ring, or if you hit an enemy, losing one point, the level is generated again. Almost the same as before.

#### How to Play
The WASD keys make the crown move. The ↑↓(pitch)→←(yaw) keys control the camera and the direction of the crown. Press Q and E to zoom out and in, respectively. Spacebar to jump. If, for whatever reason, you are not happy with the current board (too much fog?), you can hit R to restart the game and get a new board.

#### Notes
There is a lot that can still be done. But we managed to get a whole heck of a lot done.

- The collision detection on the platforms is not very reliable.
  - Sometimes clip through or jumps up on platforms.
- A large swath of terrain is generated,
  - A blendmap with 4 different textures is used to get a better looking terrain than by using a single texture.
  - A heightmap is used to generated a hilly area.
  - A bunch of trees are randomly scattered around the entire terrain.
- The entire area is surrounded with fog. The density and gradience of it is random on each restart.
- The G3DJ model loader from Kári
  - Has been adapted to read UV coords properly (was missing a letter in the key for that).
  - Now reads textures and loads them properly (assuming that the UV coords are correct in the model)
    - To be done is to allow for multiple textures for a model.
- Some rudimentary gameplay has been added.
  - Collecting something for points while avoiding enemies.
  - A simple platforming scenario with some stacks of boxes.
- 3rd person camera works pretty well.
  - The camera is, for the most part, smooth and easy to use.
    - There's a cap on how much the camera can be pitched up/down, and zoomed in/out, so not to get into some hijinks.
  - Would have been nice to manage to get it to not go into objects like boxes/enemies, but hey..
  - The camera controls are pretty good.

### TODO
- [ ] Background
  - [ ] Skybox
    - [ ] Big cube/sphere that the "board" is inside
    - [ ] Texture on the inside of the shape
  - [ ] Parallax scrolling (Optional)
- [ ] Board objects
  - [ ] Obstacles
  - [x] Platforms
- [x] Floor
  - [ ] ~Some random shape/s~
  - [x] Static board
    - [x] Texture terrain
    - [x] Trees at random
    - [x] Blend map and a few textures for less static look
    - [x] Height map for varied height
  - [ ] ~Randomly generated board~
  - [ ] ~Board generator/collector (Optional)~
    - [ ] ~Store a few pre-generated boards~
    - [ ] ~Keep track of what boards have been used~
    - [ ] ~Use some board that has not been used before~
- [ ] Enemies
  - [ ] Something to avoid (moving obstacles)
  - [ ] Can use Bezier motion here
  - [ ] Enemy that is tied to one spot (chain chomps)
  - [ ] Moving enemies (goombas)
- [x] Super Sario (character)
  - [x] Movement
  - [x] Jumping
  - [x] Design/look, shapes/texture/etc.
- [ ] Collission
  - [x] Player collission with enemies
  - [ ] Player collission with world/objects
    - [x] Floor
    - [ ] Objects/obstacles
      - [ ] Make it actually work..
- [x] Textures
  - [x] Player
  - [x] Enemies
  - [x] Objects/world
- [x] Extra stuff
  - [x] Rings to collect
  - [x] Level finish/finish condition
  - [ ] ~Level selection (not likely, but hey, maybe)~
