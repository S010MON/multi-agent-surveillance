package app.model.soundFurniture;

import app.controller.settings.SettingsObject;
import app.model.soundBoundary.SoundBoundaryFactory;

public abstract class SoundFurnitureFactory
{
    public static SoundFurniture make(SettingsObject object)
    {
        return new SoundFurnitureBase(SoundBoundaryFactory.make(object));
    }
}
