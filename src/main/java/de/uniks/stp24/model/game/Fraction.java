package de.uniks.stp24.model.game;

import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

public class Fraction {
    public static final String PROPERTY_CREATED_AT = "createdAt";
    public static final String PROPERTY_UPDATED_AT = "updatedAt";
    public static final String PROPERTY__ID = "_id";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_CAPACITY = "capacity";
    public static final String PROPERTY_UPGRADE = "upgrade";
    public static final String PROPERTY_POPULATION = "population";
    public static final String PROPERTY_X = "x";
    public static final String PROPERTY_Y = "y";
    public static final String PROPERTY_BUILDINGS = "buildings";
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_EMPIRE = "empire";
    public static final String PROPERTY_HOME_EMPIRE = "homeEmpire";
    public static final String PROPERTY_GAME = "game";
    public static final String PROPERTY_LINKS = "links";
    public static final String PROPERTY_JOBS = "jobs";
   public static final String PROPERTY_DISTRICTS = "districts";
   public static final String PROPERTY_DISTRICT_SLOTS = "districtSlots";
   public static final String PROPERTY_FLEETS = "fleets";
   public static final String PROPERTY_CURRENT_TRAVELING = "currentTraveling";
   public static final String PROPERTY_HEALTH = "health";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String _id;
    private String name;
    private int capacity;
    private String upgrade;
    private int population;
    private int x;
    private int y;
    private List<String> buildings;
    private String type;
    private Empire empire;
    private Empire homeEmpire;
    private Game game;
    private List<Fraction> links;
    protected PropertyChangeSupport listeners;
    private List<Jobs> jobs;
   private Map<String, Integer> districts;
   private Map<String, Integer> districtSlots;
   private List<Fleet> fleets;
   private List<Jobs> currentTraveling;
   private Integer health;

    public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

    public Fraction setCreatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

    public LocalDateTime getUpdatedAt()
   {
      return this.updatedAt;
   }

    public Fraction setUpdatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

    public String get_id()
   {
      return this._id;
   }

    public Fraction set_id(String value)
   {
      if (Objects.equals(value, this._id))
      {
         return this;
      }

      final String oldValue = this._id;
      this._id = value;
      this.firePropertyChange(PROPERTY__ID, oldValue, value);
      return this;
   }

    public String getName()
   {
      return this.name;
   }

    public Fraction setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

    public int getCapacity()
   {
      return this.capacity;
   }

    public Fraction setCapacity(int value)
   {
      if (value == this.capacity)
      {
         return this;
      }

      final int oldValue = this.capacity;
      this.capacity = value;
      this.firePropertyChange(PROPERTY_CAPACITY, oldValue, value);
      return this;
   }

    public String getUpgrade()
   {
      return this.upgrade;
   }

    public Fraction setUpgrade(String value)
   {
      if (Objects.equals(value, this.upgrade))
      {
         return this;
      }

      final String oldValue = this.upgrade;
      this.upgrade = value;
      this.firePropertyChange(PROPERTY_UPGRADE, oldValue, value);
      return this;
   }

    public int getPopulation()
   {
      return this.population;
   }

    public Fraction setPopulation(int value)
   {
      if (value == this.population)
      {
         return this;
      }

      final int oldValue = this.population;
      this.population = value;
      this.firePropertyChange(PROPERTY_POPULATION, oldValue, value);
      return this;
   }

    public int getX()
   {
      return this.x;
   }

    public Fraction setX(int value)
   {
      if (value == this.x)
      {
         return this;
      }

      final int oldValue = this.x;
      this.x = value;
      this.firePropertyChange(PROPERTY_X, oldValue, value);
      return this;
   }

    public int getY()
   {
      return this.y;
   }

    public Fraction setY(int value)
   {
      if (value == this.y)
      {
         return this;
      }

      final int oldValue = this.y;
      this.y = value;
      this.firePropertyChange(PROPERTY_Y, oldValue, value);
      return this;
   }

    public List<String> getBuildings()
   {
      return this.buildings != null ? Collections.unmodifiableList(this.buildings) : Collections.emptyList();
   }

    public Fraction withBuildings(String value)
   {
      if (this.buildings == null)
      {
         this.buildings = new CopyOnWriteArrayList<>();
      }
      if (this.buildings.add(value))
      {
         this.firePropertyChange(PROPERTY_BUILDINGS, null, value);
      }
      return this;
   }

    public Fraction withBuildings(String... value)
   {
      for (final String item : value)
      {
         this.withBuildings(item);
      }
      return this;
   }

    public Fraction withoutBuildings(String value)
   {
      if (this.buildings != null && this.buildings.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_BUILDINGS, value, null);
      }
      return this;
   }

    public Fraction withoutBuildings(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutBuildings(item);
      }
      return this;
   }

    public Fraction setDistricts(Map<String, Integer> value)
   {
      if (Objects.equals(value, this.districts))
      {
         return this;
      }

      final Map<String, Integer> oldValue = this.districts;
      this.districts = value;
      this.firePropertyChange(PROPERTY_DISTRICTS, oldValue, value);
      return this;
   }

    public Fraction setDistrictSlots(Map<String, Integer> value)
   {
      if (Objects.equals(value, this.districtSlots))
      {
         return this;
      }

      final Map<String, Integer> oldValue = this.districtSlots;
      this.districtSlots = value;
      this.firePropertyChange(PROPERTY_DISTRICT_SLOTS, oldValue, value);
      return this;
   }

    public String getType()
   {
      return this.type;
   }

    public Fraction setType(String value)
   {
      if (Objects.equals(value, this.type))
      {
         return this;
      }

      final String oldValue = this.type;
      this.type = value;
      this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
      return this;
   }

    public Empire getEmpire()
   {
      return this.empire;
   }

    public Fraction setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.withoutFractions(this);
      }
      this.empire = value;
      if (value != null)
      {
         value.withFractions(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

    public Fraction setHomeEmpire(Empire value)
   {
      if (this.homeEmpire == value)
      {
         return this;
      }

      final Empire oldValue = this.homeEmpire;
      if (this.homeEmpire != null)
      {
         this.homeEmpire = null;
         oldValue.setHomeSystem(null);
      }
      this.homeEmpire = value;
      if (value != null)
      {
         value.setHomeSystem(this);
      }
      this.firePropertyChange(PROPERTY_HOME_EMPIRE, oldValue, value);
      return this;
   }

    public Game getGame()
   {
      return this.game;
   }

    public Fraction setGame(Game value)
   {
      if (this.game == value)
      {
         return this;
      }

      final Game oldValue = this.game;
      if (this.game != null)
      {
         this.game = null;
         oldValue.withoutFractions(this);
      }
      this.game = value;
      if (value != null)
      {
         value.withFractions(this);
      }
      this.firePropertyChange(PROPERTY_GAME, oldValue, value);
      return this;
   }

    public List<Fraction> getLinks()
   {
      return this.links != null ? Collections.unmodifiableList(this.links) : Collections.emptyList();
   }

    public Fraction withLinks(Fraction value)
   {
      if (this.links == null)
      {
         this.links = new CopyOnWriteArrayList<>();
      }
      if (!this.links.contains(value))
      {
         this.links.add(value);
         value.withLinks(this);
         this.firePropertyChange(PROPERTY_LINKS, null, value);
      }
      return this;
   }

    public List<Jobs> getJobs()
   {
      return this.jobs != null ? Collections.unmodifiableList(this.jobs) : Collections.emptyList();
   }

    public Fraction withJobs(Jobs value)
   {
      if (this.jobs == null)
      {
         this.jobs = new CopyOnWriteArrayList<>();
      }
      if (!this.jobs.contains(value))
      {
         this.jobs.add(value);
         value.setFraction(this);
         this.firePropertyChange(PROPERTY_JOBS, null, value);
      }
      return this;
   }

    public Fraction withoutJobs(Jobs value)
   {
      if (this.jobs != null && this.jobs.remove(value))
      {
         value.setFraction(null);
         this.firePropertyChange(PROPERTY_JOBS, value, null);
      }
      return this;
   }

   public Map<String, Integer> getDistricts()
   {
      return this.districts;
   }

   public Map<String, Integer> getDistrictSlots()
   {
      return this.districtSlots;
   }

   public List<Fleet> getFleets()
   {
      return this.fleets != null ? Collections.unmodifiableList(this.fleets) : Collections.emptyList();
   }

   public Fraction withFleets(Fleet value)
   {
      if (this.fleets == null)
      {
         this.fleets = new CopyOnWriteArrayList<>();
      }
      if (!this.fleets.contains(value))
      {
         this.fleets.add(value);
         value.setLocation(this);
         this.firePropertyChange(PROPERTY_FLEETS, null, value);
      }
      return this;
   }

   public Fraction withoutFleets(Fleet value)
   {
      if (this.fleets != null && this.fleets.remove(value))
      {
         value.setLocation(null);
         this.firePropertyChange(PROPERTY_FLEETS, value, null);
      }
      return this;
   }

   public List<Jobs> getCurrentTraveling()
   {
      return this.currentTraveling != null ? Collections.unmodifiableList(this.currentTraveling) : Collections.emptyList();
   }

   public Fraction withCurrentTraveling(Jobs value)
   {
      if (this.currentTraveling == null)
      {
         this.currentTraveling = new CopyOnWriteArrayList<>();
      }
      if (!this.currentTraveling.contains(value))
      {
         this.currentTraveling.add(value);
         value.withPath(this);
         this.firePropertyChange(PROPERTY_CURRENT_TRAVELING, null, value);
      }
      return this;
   }

   public Fraction withoutCurrentTraveling(Jobs value)
   {
      if (this.currentTraveling != null && this.currentTraveling.remove(value))
      {
         value.withoutPath(this);
         this.firePropertyChange(PROPERTY_CURRENT_TRAVELING, value, null);
      }
      return this;
   }
   public Integer getHealth()
   {
      return this.health;
   }

   public Fraction setHealth(Integer value)
   {
      if (Objects.equals(value, this.health))
      {
         return this;
      }

      final Integer oldValue = this.health;
      this.health = value;
      this.firePropertyChange(PROPERTY_HEALTH, oldValue, value);
      return this;
   }

   public Fraction withBuildings(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withBuildings(item);
      }
      return this;
   }

   public Fraction withoutBuildings(String... value)
   {
      for (final String item : value)
      {
         this.withoutBuildings(item);
      }
      return this;
   }

   public Fraction withFleets(Fleet... value)
   {
      for (final Fleet item : value)
      {
         this.withFleets(item);
      }
      return this;
   }

   public Fraction withFleets(Collection<? extends Fleet> value)
   {
      for (final Fleet item : value)
      {
         this.withFleets(item);
      }
      return this;
   }

   public Fraction withoutFleets(Fleet... value)
   {
      for (final Fleet item : value)
      {
         this.withoutFleets(item);
      }
      return this;
   }

   public Fraction withoutFleets(Collection<? extends Fleet> value)
   {
      for (final Fleet item : value)
      {
         this.withoutFleets(item);
      }
      return this;
   }

   public Fraction withJobs(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Fraction withJobs(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Fraction withoutJobs(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public Fraction withoutJobs(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public Fraction withCurrentTraveling(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withCurrentTraveling(item);
      }
      return this;
   }

   public Fraction withCurrentTraveling(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withCurrentTraveling(item);
      }
      return this;
   }

   public Fraction withoutCurrentTraveling(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withoutCurrentTraveling(item);
      }
      return this;
   }

   public Fraction withoutCurrentTraveling(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withoutCurrentTraveling(item);
      }
      return this;
   }

   public Empire getHomeEmpire()
   {
      return this.homeEmpire;
   }

   public Fraction withLinks(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withLinks(item);
      }
      return this;
   }

   public Fraction withLinks(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withLinks(item);
      }
      return this;
   }

   public Fraction withoutLinks(Fraction value)
   {
      if (this.links != null && this.links.remove(value))
      {
         value.withoutLinks(this);
         this.firePropertyChange(PROPERTY_LINKS, value, null);
      }
      return this;
   }

   public Fraction withoutLinks(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withoutLinks(item);
      }
      return this;
   }

   public Fraction withoutLinks(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withoutLinks(item);
      }
      return this;
   }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

    public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

    @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.get_id());
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getUpgrade());
      result.append(' ').append(this.getBuildings());
      result.append(' ').append(this.getType());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutFleets(new ArrayList<>(this.getFleets()));
      this.withoutJobs(new ArrayList<>(this.getJobs()));
      this.withoutCurrentTraveling(new ArrayList<>(this.getCurrentTraveling()));
      this.setEmpire(null);
      this.setHomeEmpire(null);
      this.setGame(null);
      this.withoutLinks(new ArrayList<>(this.getLinks()));
   }
}