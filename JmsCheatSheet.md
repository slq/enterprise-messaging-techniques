# Useful links

* [Wirting on github](https://help.github.com/categories/writing-on-github/)
* [Basic formatting](https://help.github.com/articles/basic-writing-and-formatting-syntax/)
* [Code blocks](https://help.github.com/articles/creating-and-highlighting-code-blocks/)
* [Tables](https://help.github.com/articles/organizing-information-with-tables/)
* [Mastering Markdown](https://guides.github.com/features/mastering-markdown/)

## JMS History

* JMS 1.02b (JSR 58) June 2001
* JMS 1.1 (JSR 914) March 2002
* JMS 2.0 (JSR 343) May 2013

## Messaging model

* Point to point
  * Terminology
    * Queue
    * Sender
    * Receiver
  * (Sender vs Receiver) or (Producer,Publisher vs Consumer from JMS 1.1)
  * Message delivered to only one consumer
  * Fire-and-forget (Asynchronous)
  * Request-and-reply - need two queues (Synchronous, blocking)
  * Load balancing

![Point-to-point](https://github.com/slq/jms-wiki/blob/messaging-models/src/main/resources/point-to-point.PNG)

* Publish and Subscribe
  * Terminology
    * Queue -> Topic
    * Sender -> Publisher
    * Receiver -> Subscriber
  * Message delivered to each subscriber
  * Fire-and-forget broadcast
  * Load balancing of subscribers (JMS 2.0)
  * Subscriber is not known by publisher
  * Queue depth vs Topic depth - there is no topic depth, subscribers can have their own queues

![Publish-subscribe](https://github.com/slq/jms-wiki/blob/messaging-models/src/main/resources/publish-subscribe.PNG)

## JMS message structure
* Header
  * standard properties
  * extended properties
  * app-specific properties
  * vendor-specific properties
* Body
  * text payload - interface `javax.jms.TextMessage`
    * good portability to other protocols (e.g., STOMP)
    * can be used for XML
    * `msg.setText(String)`, `msg.getText()`
  * java object payload - interface `javax.jms.ObjectMessage`
    * limited portability - java to java only
    * object must implement `java.io.Serializable` for marshalling
    * `msg.setObject(Object)`, `msg.getObject()`
  * map payload - interface `javax.jms.MapMessage`
    * must have an agreed upon contract of property types and values and meaning
    * good extensibility and flexibility
    * good when contract changes frequently
    * `msg.setString/setLong...(key, value)`, `msg.getString/getLong...()`
  * byte payload - interface `javax.jms.BytesMessage`
    * must have an agreed upon order of bytes
    * highest porability for JMS messages
    * `msg.writeBytes(byte[])`, `msg.writeUTF()`, `msg.writeInt()`, `msg.readBytes()`, `msg.readUTF()`, `msg.readInt()`
  * stream payload - interface `javax.jms.StreamMessage`
    * allows types conversion
    * like `javax.jms.ObjectMessage` is for java to java only
    * `msg.writeBytes(byte[])`, `msg.writeString()`, `msg.writeInt()`, `msg.readBytes()`, `msg.readString()`, `msg.readInt()`
  * event payload - interface `javax.jms.Message`
    * general interface
    * can be used as event notification message or trigger instead of using `javax.jms.TextMessage`
      * set properties in header (optional)
      * leave body empty - no payload

![Jms-message-structure](https://github.com/slq/jms-wiki/blob/master/src/main/resources/jms-message-structure.PNG)

## Guaranteed delivery
* Requires message persistence
* Only persistent messages survive system failures
* Performance hit
* Routine
  * Happy path
    1. Send: message producer -> jms provider
    2. Persist: jms provider -> persistent store
    3. Ack: jms provider -> message producer
    4. Deliver: jms provider -> message consumer
    5. Ack: message consumer -> jms provider
    6. Delete: jms provider -> persistent store
  * System failure before ack
    * Exception sent to producer - message is still in producer
    * Message not persisted
  * System failure after ack
    * Message is persisted
    * Message is delivered to consumer after system is restored
* Routine (no persistency)
  * Happy path
    1. Send ->
    2. Ack <-
    3. Deliver ->
    4. Ack <-
  * System failure before ack
    * Exception sent to producer - message is still in producer
  * System failure after ack
    * Message is in memory
    * Message is lost, never delivered to consumer

![Jms-message-structure](https://github.com/slq/jms-wiki/blob/master/src/main/resources/jms-persistent-messages.PNG)
