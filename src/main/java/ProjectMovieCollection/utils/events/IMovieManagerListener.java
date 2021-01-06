package ProjectMovieCollection.utils.events;

public interface IMovieManagerListener {

    /**
     * Called when the progress of loading changes
     * @param progress The new progress value
     */
    void updateLoadProgress(float progress);

}
