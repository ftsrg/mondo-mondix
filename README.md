# Mondix – the MONDO Indexer

## Key notions of mondix 

The **mondix** API is a **uniform interface** for publishing knowledge in a **read-only** way. 

The mondix interface is intended to be implemented by **model indexers** and other sources of knowledge in a scalable model management context; such mondix backends are sometimes called **mondixers**. 

The mondix interface is intended to be used by **mondix clients** that extract and process knowledge published by mondixers; a typical mondix client could be a **complex model query engine** that performs computations and provides results to users based on information (such as knowledge about models) provided by one or more mondixers.

## Overview of data model and operations of mondix

A mondixer publishes knowledge in a **relational format**, as a set of named **base relations**. The names of published relations is exposed through a special **catalog relation** (with an empty string for name). Each base relation defines an ordered list of named columns. A base relation is conceptually said to contain rows, each of which is a tuple containing a value for each column of the relation. 

The client can open **relational views** from a base relation. A view indicates a specific intention of the client to obtain knowledge from a base relation, and may be (should be) disposed if no longer relevant for the client. Each view specifies a _filtered relational projection_ on the base relation that (a) considers only those rows of the relation that take given values at given columns, while taking arbitrary values at the other columns; (b) exposes only the given subset of relation columns in query results; (c) is a relation, i.e. without duplicate rows. 

Having specified views, the client can invoke at least two **query operations** to actually obtain results: retrieving all (filtered, projected) rows of the view, and counting the number of such rows (without enumerating them all).
 
## Guarantees and responsibilities 

**Mondixers publish knowledge** through the mondix API. Thus they are the components responsible for the **comprehension & uniformization** of some data source. In a scalable model management context, there may be many model repositories avaliable with heterogenous model representations, so it is the responsibility of the mondixer to expose all this knowledge through a single API, so that the complex query engine can treat model uniformly. This might require a conceptual transformation of the data, such as ORM (Object-Relational Mapping).

**Mondixers guarantee fast answers** for the core query operations of the mondix interface. This means that the execution time of such an operation can grow at most proportionally to the size of the result or return value of that operation („big O” bound), regardless of the size of the irrelevant parts of knowledge. As an exception, a **one-time initialization cost** exceeding this bound is allowed upon the creation of a view, e.g. for constructing the appropriate lookup structures. This guarantee is backed by the intention that mondixer are typically model indexers that are designed to provide such operations efficiently, and is mandatory so that complex query engines can operate without having to guess the internal efficiency of mondixers.

The complex query engines access knowledge published by one or more mondixers (potentially at different locations in a distributed system) through the mondix interface, and process it to compute query results. It is the responsibility of such mondix clients to allow the **formulation and interpretation and execution of complex queries** that refer to knowledge provided by the mondixers, where the query execution phase may consult the mondixers through the mondix API. It is the responsibility of the query engine to plan, distribute and schedule computation steps and intermediate results; complex query processing is not expected of mondixers. 

## Modularity

The mondix API is envisioned as modular: not all mondixers are required to provide every capability, as not all clients would need them all. The following feature packs are foreseen at the moment:

* (Core query interface)
* Change-aware Extensions
* Multi-threading Extensions
* Access Control Extensions
* Transactional Extensions
* Configuration & Lifecycle Extensions
* Metadata Extensions
* Distributed Processing Extensions

Currently, the draft API is available for the core functionality and the change-aware extensions. The rest of the extensions are to be designed in the future.

## Change-aware extensions 

The core functionality assumes that the knowledge indexed by the mondixer does not change during the time frame of the activities of the client (more precisely, during the lifespan of a view). There are cases when this assumption cannot be made. The indexed model, or at least some base relations provided by the mondixers, may experience change. In this case, it can often be useful to notify clients of this change, so that the complex query results computed from the output of the mondixer can be kept up to date.

If a mondix base relation is change-aware, its views are **live views**. A live view accepts **change listeners**, which will be notified of rows added to and removed from of the results of the view (updated rows are represented as a removal and a reinsertion). If a complex query engine registers such change listeners, it can update its own complex query results, thereby realizing **incremental query evaluation**.

A meaningful modification of the indexed model may involve more than one row-level change in the relational representation. Thus the client may have an **inconsistent picture** of the published knowledge when it has only partially received change notifications from the various live queries of the various change-aware base relations. Therefore change-aware relations must be published by **change-aware mondix instance**s that provide a **consistency listener** facility. When the indexed model reaches a consistent state after a sequence of changes (of which clients have been notified via the change listeners registered at live queries), the change aware mondix instance notifies the consistency listeners of this fact. The exact guarantees of the consistency notification (described in detail in the Javadoc) take into account the case of multiple consistency listeners that may change the model.


## Contents 

* `eu.mondo.mondix.interfaces`: the mondix interfaces themselves
  - still a draft version 
  - core interfaces and change-aware extensions available currently 
* `eu.mondo.mondix.implementation.hashmap`: a simple default implementation that serves sets of in-memory rows as mondix relations
  - one can copy some data source into this relation, and it will be served via mondix 
  - change-aware parts not very well-done yet 
  - not very extensible yet 
  - **Planned**: flexible component library, e.g.
    * An *indexer* implementation to host any base relations 
      - Add base relations through separate config interface    
    * Default *base relation* implementation 
      - Serves internal in-memory table only    
      - Contents editable through separate manipulation interface    
    * Allow „roll your own” *base relation* implementations 
      - May be based on e.g. an RDF store, Hawk, etc.    
      - Makes use of library for projections, lookup tables, etc.    
* `eu.mondo.mondix.client`: basically a JUnit test case for the „hashmap” mondixer
    - misleading name: this is not a mondix client library 
* `eu.mondo.mondix.fourstore`: empty placeholder project for a 4store-backed mondixer
* `eu.mondo.mondix.incquery`: runtime component of mondix bindings for IncQuery 
  - enables IncQuery as a mondix client, providing complex query capabilities on top of mondix
  - very early draft version
  - can formulate and evaluate queries over mondix relations via IncQuery
  - requires the `api-refactor0.9` branch of IncQuery 
* `eu.mondo.mondix.incquery.test`: JUnit test for the IncQuery binding
