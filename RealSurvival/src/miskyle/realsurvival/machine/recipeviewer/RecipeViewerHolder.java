package miskyle.realsurvival.machine.recipeviewer;

public class RecipeViewerHolder extends RecipeAlbumHolder {
  
  public RecipeViewerHolder() {
    setMenu(false);
  }
  
  @Override
  public boolean isMenu() {
    return false;
  }
}
