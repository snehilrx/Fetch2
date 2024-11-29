package model

import kotlinx.serialization.Serializable


@Serializable
sealed class ServerLinks(open val serverName: String) {

    class OnlineServerLink(override val serverName: String, val link: String) :
        ServerLinks(serverName) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OnlineServerLink) return false
            return link == other.link
        }

        override fun hashCode(): Int {
            return link.hashCode()
        }
    }

    class OfflineServerLink(override val serverName: String, val download: Download) :
        ServerLinks(serverName) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfflineServerLink) return false
            return serverName == other.serverName
        }

        override fun hashCode(): Int {
            return serverName.hashCode()
        }
    }


}