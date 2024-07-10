package di

import com.example.data.workers.ChildWorkerFactory
import com.example.data.workers.RefreshForecastDataWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(RefreshForecastDataWorker::class)
    fun bindRefreshForecastDataWorker(worker: RefreshForecastDataWorker.Factory): ChildWorkerFactory

}