@echo off
git checkout runelite
git pull origin master
pause
git add .
git commit -m "Merge origin master"
git push origin runelite
pause
git pull https://github.com/runelite/runelite.git master
echo Please check for merge conflicts now
echo Please check for merge conflicts now
echo Please check for merge conflicts now
echo Please check for merge conflicts now
echo Please check for merge conflicts now
pause
git add .
git commit -m "Merge runelite/runelite master"
git push origin runelite
pause
