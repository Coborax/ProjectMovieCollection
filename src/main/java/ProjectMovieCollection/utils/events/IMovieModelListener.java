package ProjectMovieCollection.utils.events;

public interface IMovieModelListener {

    /**
     * Called when all movies data has been fetched
     */
    void dataFetched();

    /**
     * Called when the progress of loading changes
     * @param progress The new progress value
     */
    void updateLoadProgress(float progress);

}
