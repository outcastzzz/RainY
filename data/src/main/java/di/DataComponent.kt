package di

import dagger.Subcomponent

@Subcomponent
interface DataComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DataComponent
    }

}