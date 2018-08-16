package bitcoin.wallet.kit.network

import bitcoin.walllet.kit.network.MessageSender
import bitcoin.walllet.kit.network.PeerGroupListener
import bitcoin.walllet.kit.network.PeerListener
import bitcoin.walllet.kit.network.message.Message
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class PeerGroup(private val peerGroupListener: PeerGroupListener, private val peerManager: PeerManager, private val peerSize: Int = 3) : Thread(), PeerListener {

    private val log = LoggerFactory.getLogger(PeerGroup::class.java)
    private val peerMap = ConcurrentHashMap<String, Peer>()
    private var syncPeer: Peer? = null

    @Volatile
    private var running = false

    override fun run() {
        running = true
        // loop:
        while (running) {
            if (peerMap.size < peerSize) {
                startConnection()
            }

            try {
                Thread.sleep(5000L)
            } catch (e: InterruptedException) {
                break
            }
        }

        log.info("Closing all peer connections...")
        for (conn in peerMap.values) {
            conn.close()
        }
    }

    private fun startConnection() {
        log.info("Try open new peer connection...")
        val ip = peerManager.getPeerIp()
        if (ip != null) {
            log.info("Try open new peer connection to $ip...")
            val peer = Peer(ip, this)
            peerMap[ip] = peer
            peer.start()
        } else {
            log.info("No peers found yet.")
        }
    }

    fun close() {
        running = false
        interrupt()
        try {
            join(5000)
        } catch (e: InterruptedException) {
        }
    }

    /**
     * Send message to all connected peers.
     *
     * @param message
     * Bitcoin message object.
     * @return Number of peers sent.
     */
    fun sendMessage(message: Message): Int {
        var n = 0
        for (sender in peerMap.values) {
            sender.sendMessage(message)
            n++
        }

        return n
    }

    override fun onMessage(sender: MessageSender, message: Message) {
        peerGroupListener.onMessage(sender, message)
    }

    override fun connected(peer: Peer) {
        if (syncPeer == null) {
            syncPeer = peer

            log.info("Sync Peer ready")
            peerGroupListener.onReady()
        }
    }

    override fun disconnected(peer: Peer, e: Exception?) {
        if (e == null) {
            log.info("PeerAddress $peer.host disconnected.")
            peerManager.markSuccess(peer.host)
        } else {
            log.warn("PeerAddress $peer.host disconnected with error.", e.message)
            peerManager.markFailed(peer.host)
        }

        // it restores syncPeer on next connection
        if (syncPeer == peer) {
            syncPeer = null
        }

        peerMap.remove(peer.host)
    }
}