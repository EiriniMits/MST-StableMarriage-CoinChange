# MST-StableMarriage-CoinChange
This project was created for academic purposes on the subject of 'Algorithms' at the Aristotle University of Thessaloniki.

In this project are implemented algorithms for the solution of the MST, Stable Marriage and Coin Change problems. In this implementation of the algorithms we create an ant colony of black and red ants.

1. Each ants position is determined by (x,y) coordinates.
2. The black ants collect the food and the red ants carry it back tou the anthill.
3. The number of black ants equals to the red ants number.
4. Each red ant carry a bucket of different capacity.
5. Black ants collect seed from multiple species.
6. Each type of seed has a different weight.
7. Each black ant can choose seeds from 5 different species.

So the data has the following form:
```
Category    Coordinates    Details

0(red)      0.123 0.8765   1567 (Bucket Capacity) 
1(black)    0.821 0.5262   12 14 34 46 24 (Weight per seed) 
0(red)      0.345 0.6534   1002 (Bucket Capacity) 
1(black)    0.812 0.9134   42 44 24 26 14 (Weight per seed) 
...
```
