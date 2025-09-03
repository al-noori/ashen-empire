package de.uniks.stp24.model.game;

import java.beans.PropertyChangeSupport;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

public class Empire
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_DESCRIPTION = "description";
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_FLAG = "flag";
   public static final String PROPERTY_PORTRAIT = "portrait";
   public static final String PROPERTY_OWNER = "owner";
   public static final String PROPERTY_HOME_SYSTEM = "homeSystem";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_TRAITS = "traits";
   public static final String PROPERTY_FRACTIONS = "fractions";
   public static final String PROPERTY_JOBS = "jobs";
   public static final String PROPERTY_RESOURCES = "resources";
   public static final String PROPERTY_TECHNOLOGIES = "technologies";
   public static final String PROPERTY_SHIPS = "ships";
   public static final String PROPERTY_FLEETS = "fleets";
   public static final String PROPERTY_DEFENDING_WARS = "defendingWars";
   public static final String PROPERTY_ATTACKING_WARS = "attackingWars";
   private String name;
   private String description;
   private String color;
   private int flag;
   private int portrait;
   private User owner;
   protected PropertyChangeSupport listeners;
   private Fraction homeSystem;
   private String _id;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private List<String> traits;
   private List<Fraction> fractions;
   private List<Jobs> jobs;
   private Map<String, Integer> resources;
   private List<Technology> technologies;
   private List<Ship> ships;
   private List<Fleet> fleets;
   private List<War> defendingWars;
   private List<War> attackingWars;

   public String getName()
   {
      return this.name;
   }

   public Empire setName(String value)
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

   public String getDescription()
   {
      return this.description;
   }

   public Empire setDescription(String value)
   {
      if (Objects.equals(value, this.description))
      {
         return this;
      }

      final String oldValue = this.description;
      this.description = value;
      this.firePropertyChange(PROPERTY_DESCRIPTION, oldValue, value);
      return this;
   }

   public String getColor()
   {
      return this.color;
   }

   public Empire setColor(String value)
   {
      if (Objects.equals(value, this.color))
      {
         return this;
      }

      final String oldValue = this.color;
      this.color = value;
      this.firePropertyChange(PROPERTY_COLOR, oldValue, value);
      return this;
   }

   public int getFlag()
   {
      return this.flag;
   }

   public Empire setFlag(int value)
   {
      if (value == this.flag)
      {
         return this;
      }

      final int oldValue = this.flag;
      this.flag = value;
      this.firePropertyChange(PROPERTY_FLAG, oldValue, value);
      return this;
   }

   public int getPortrait()
   {
      return this.portrait;
   }

   public Empire setPortrait(int value)
   {
      if (value == this.portrait)
      {
         return this;
      }

      final int oldValue = this.portrait;
      this.portrait = value;
      this.firePropertyChange(PROPERTY_PORTRAIT, oldValue, value);
      return this;
   }

   public User getOwner()
   {
      return this.owner;
   }

   public Empire setOwner(User value)
   {
      if (this.owner == value)
      {
         return this;
      }

      final User oldValue = this.owner;
      if (this.owner != null)
      {
         this.owner = null;
         oldValue.setEmpire(null);
      }
      this.owner = value;
      if (value != null)
      {
         value.setEmpire(this);
      }
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
      return this;
   }

   public Fraction getHomeSystem()
   {
      return this.homeSystem;
   }

   public Empire setHomeSystem(Fraction value)
   {
      if (this.homeSystem == value)
      {
         return this;
      }

      final Fraction oldValue = this.homeSystem;
      if (this.homeSystem != null)
      {
         this.homeSystem = null;
         oldValue.setHomeEmpire(null);
      }
      this.homeSystem = value;
      if (value != null)
      {
         value.setHomeEmpire(this);
      }
      this.firePropertyChange(PROPERTY_HOME_SYSTEM, oldValue, value);
      return this;
   }

   public String get_id()
   {
      return this._id;
   }

   public Empire set_id(String value)
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

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Empire setCreatedAt(LocalDateTime value)
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

   public Empire setUpdatedAt(LocalDateTime value)
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

   public List<String> getTraits()
   {
      return this.traits != null ? Collections.unmodifiableList(this.traits) : Collections.emptyList();
   }

   public List<Fraction> getFractions()
   {
      return this.fractions != null ? Collections.unmodifiableList(this.fractions) : Collections.emptyList();
   }

   public Empire withFractions(Fraction value)
   {
      if (this.fractions == null)
      {
         this.fractions = new CopyOnWriteArrayList<>();
      }
      if (!this.fractions.contains(value))
      {
         this.fractions.add(value);
         value.setEmpire(this);
         this.firePropertyChange(PROPERTY_FRACTIONS, null, value);
      }
      return this;
   }

   public Empire withoutFractions(Fraction value)
   {
      if (this.fractions != null && this.fractions.remove(value))
      {
         value.setEmpire(null);
         this.firePropertyChange(PROPERTY_FRACTIONS, value, null);
      }
      return this;
   }

   public Empire withoutFractions(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withoutFractions(item);
      }
      return this;
   }

   public Empire setResources(Map<String, Integer> value)
   {
      if (Objects.equals(value, this.resources))
      {
         return this;
      }

      final Map<String, Integer> oldValue = this.resources;
      this.resources = value;
      this.firePropertyChange(PROPERTY_RESOURCES, oldValue, value);
      return this;
   }

   public List<Jobs> getJobs()
   {
      return this.jobs != null ? Collections.unmodifiableList(this.jobs) : Collections.emptyList();
   }

   public Empire withJobs(Jobs value)
   {
      if (this.jobs == null)
      {
         this.jobs = new CopyOnWriteArrayList<>();
      }
      if (!this.jobs.contains(value))
      {
         this.jobs.add(value);
         value.setEmpire(this);
         this.firePropertyChange(PROPERTY_JOBS, null, value);
      }
      return this;
   }

   public Empire withoutJobs(Jobs value)
   {
      if (this.jobs != null && this.jobs.remove(value))
      {
         value.setEmpire(null);
         this.firePropertyChange(PROPERTY_JOBS, value, null);
      }
      return this;
   }

   public Empire withoutJobs(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public Map<String, Integer> getResources()
   {
      return this.resources;
   }

   public List<Technology> getTechnologies()
   {
      return this.technologies != null ? Collections.unmodifiableList(this.technologies) : Collections.emptyList();
   }

   public Empire withTechnologies(Technology value)
   {
      if (this.technologies == null)
      {
         this.technologies = new CopyOnWriteArrayList<>();
      }
      if (!this.technologies.contains(value))
      {
         this.technologies.add(value);
         value.setEmpire(this);
         this.firePropertyChange(PROPERTY_TECHNOLOGIES, null, value);
      }
      return this;
   }

   public Empire withTechnologies(Collection<? extends Technology> value)
   {
      for (final Technology item : value)
      {
         this.withTechnologies(item);
      }
      return this;
   }

   public Empire withoutTechnologies(Technology value)
   {
      if (this.technologies != null && this.technologies.remove(value))
      {
         value.setEmpire(null);
         this.firePropertyChange(PROPERTY_TECHNOLOGIES, value, null);
      }
      return this;
   }

   public Empire withoutTechnologies(Collection<? extends Technology> value)
   {
      for (final Technology item : value)
      {
         this.withoutTechnologies(item);
      }
      return this;
   }

   public List<Ship> getShips()
   {
      return this.ships != null ? Collections.unmodifiableList(this.ships) : Collections.emptyList();
   }

   public Empire withShips(Ship value)
   {
      if (this.ships == null)
      {
         this.ships = new CopyOnWriteArrayList<>();
      }
      if (!this.ships.contains(value))
      {
         this.ships.add(value);
         value.setEmpire(this);
         this.firePropertyChange(PROPERTY_SHIPS, null, value);
      }
      return this;
   }

   public Empire withoutShips(Ship value)
   {
      if (this.ships != null && this.ships.remove(value))
      {
         value.setEmpire(null);
         this.firePropertyChange(PROPERTY_SHIPS, value, null);
      }
      return this;
   }

   public Empire withoutShips(Collection<? extends Ship> value)
   {
      for (final Ship item : value)
      {
         this.withoutShips(item);
      }
      return this;
   }

   public List<Fleet> getFleets()
   {
      return this.fleets != null ? Collections.unmodifiableList(this.fleets) : Collections.emptyList();
   }

   public Empire withFleets(Fleet value)
   {
      if (this.fleets == null)
      {
         this.fleets = new CopyOnWriteArrayList<>();
      }
      if (!this.fleets.contains(value))
      {
         this.fleets.add(value);
         value.setEmpire(this);
         this.firePropertyChange(PROPERTY_FLEETS, null, value);
      }
      return this;
   }

   public Empire withoutFleets(Fleet value)
   {
      if (this.fleets != null && this.fleets.remove(value))
      {
         value.setEmpire(null);
         this.firePropertyChange(PROPERTY_FLEETS, value, null);
      }
      return this;
   }

   public Empire withoutFleets(Collection<? extends Fleet> value)
   {
      for (final Fleet item : value)
      {
         this.withoutFleets(item);
      }
      return this;
   }

   public List<War> getDefendingWars()
   {
      return this.defendingWars != null ? Collections.unmodifiableList(this.defendingWars) : Collections.emptyList();
   }

   public Empire withDefendingWars(War value)
   {
      if (this.defendingWars == null)
      {
         this.defendingWars = new CopyOnWriteArrayList<>();
      }
      if (!this.defendingWars.contains(value))
      {
         this.defendingWars.add(value);
         value.setDefender(this);
         this.firePropertyChange(PROPERTY_DEFENDING_WARS, null, value);
      }
      return this;
   }

   public Empire withoutDefendingWars(War value)
   {
      if (this.defendingWars != null && this.defendingWars.remove(value))
      {
         value.setDefender(null);
         this.firePropertyChange(PROPERTY_DEFENDING_WARS, value, null);
      }
      return this;
   }

   public Empire withoutDefendingWars(Collection<? extends War> value)
   {
      for (final War item : value)
      {
         this.withoutDefendingWars(item);
      }
      return this;
   }

   public List<War> getAttackingWars()
   {
      return this.attackingWars != null ? Collections.unmodifiableList(this.attackingWars) : Collections.emptyList();
   }

   public Empire withAttackingWars(War value)
   {
      if (this.attackingWars == null)
      {
         this.attackingWars = new CopyOnWriteArrayList<>();
      }
      if (!this.attackingWars.contains(value))
      {
         this.attackingWars.add(value);
         value.setAttacker(this);
         this.firePropertyChange(PROPERTY_ATTACKING_WARS, null, value);
      }
      return this;
   }
   public Empire withoutAttackingWars(War value)
   {
      if (this.attackingWars != null && this.attackingWars.remove(value))
      {
         value.setAttacker(null);
         this.firePropertyChange(PROPERTY_ATTACKING_WARS, value, null);
      }
      return this;
   }

   public Empire withoutAttackingWars(Collection<? extends War> value)
   {
      for (final War item : value)
      {
         this.withoutAttackingWars(item);
      }
      return this;
   }

   public Empire withTraits(String value)
   {
      if (this.traits == null)
      {
         this.traits = new CopyOnWriteArrayList<>();
      }
      if (this.traits.add(value))
      {
         this.firePropertyChange(PROPERTY_TRAITS, null, value);
      }
      return this;
   }

   public Empire withTraits(String... value)
   {
      for (final String item : value)
      {
         this.withTraits(item);
      }
      return this;
   }

   public Empire withTraits(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withTraits(item);
      }
      return this;
   }

   public Empire withoutTraits(String value)
   {
      if (this.traits != null && this.traits.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_TRAITS, value, null);
      }
      return this;
   }

   public Empire withoutTraits(String... value)
   {
      for (final String item : value)
      {
         this.withoutTraits(item);
      }
      return this;
   }

   public Empire withoutTraits(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutTraits(item);
      }
      return this;
   }

   public Empire withDefendingWars(War... value)
   {
      for (final War item : value)
      {
         this.withDefendingWars(item);
      }
      return this;
   }

   public Empire withDefendingWars(Collection<? extends War> value)
   {
      for (final War item : value)
      {
         this.withDefendingWars(item);
      }
      return this;
   }

   public Empire withoutDefendingWars(War... value)
   {
      for (final War item : value)
      {
         this.withoutDefendingWars(item);
      }
      return this;
   }

   public Empire withAttackingWars(War... value)
   {
      for (final War item : value)
      {
         this.withAttackingWars(item);
      }
      return this;
   }

   public Empire withAttackingWars(Collection<? extends War> value)
   {
      for (final War item : value)
      {
         this.withAttackingWars(item);
      }
      return this;
   }

   public Empire withoutAttackingWars(War... value)
   {
      for (final War item : value)
      {
         this.withoutAttackingWars(item);
      }
      return this;
   }

   public Empire withShips(Ship... value)
   {
      for (final Ship item : value)
      {
         this.withShips(item);
      }
      return this;
   }

   public Empire withShips(Collection<? extends Ship> value)
   {
      for (final Ship item : value)
      {
         this.withShips(item);
      }
      return this;
   }

   public Empire withoutShips(Ship... value)
   {
      for (final Ship item : value)
      {
         this.withoutShips(item);
      }
      return this;
   }

   public Empire withFleets(Fleet... value)
   {
      for (final Fleet item : value)
      {
         this.withFleets(item);
      }
      return this;
   }

   public Empire withFleets(Collection<? extends Fleet> value)
   {
      for (final Fleet item : value)
      {
         this.withFleets(item);
      }
      return this;
   }

   public Empire withoutFleets(Fleet... value)
   {
      for (final Fleet item : value)
      {
         this.withoutFleets(item);
      }
      return this;
   }

   public Empire withTechnologies(Technology... value)
   {
      for (final Technology item : value)
      {
         this.withTechnologies(item);
      }
      return this;
   }

   public Empire withoutTechnologies(Technology... value)
   {
      for (final Technology item : value)
      {
         this.withoutTechnologies(item);
      }
      return this;
   }

   public Empire withJobs(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Empire withJobs(Collection<? extends Jobs> value)
   {
      for (final Jobs item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Empire withoutJobs(Jobs... value)
   {
      for (final Jobs item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public Empire withFractions(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withFractions(item);
      }
      return this;
   }

   public Empire withFractions(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withFractions(item);
      }
      return this;
   }

   public Empire withoutFractions(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withoutFractions(item);
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
      result.append(' ').append(this.getDescription());
      result.append(' ').append(this.getColor());
      result.append(' ').append(this.getTraits());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutDefendingWars(new ArrayList<>(this.getDefendingWars()));
      this.withoutAttackingWars(new ArrayList<>(this.getAttackingWars()));
      this.withoutShips(new ArrayList<>(this.getShips()));
      this.withoutFleets(new ArrayList<>(this.getFleets()));
      this.withoutTechnologies(new ArrayList<>(this.getTechnologies()));
      this.withoutJobs(new ArrayList<>(this.getJobs()));
      this.setOwner(null);
      this.withoutFractions(new ArrayList<>(this.getFractions()));
      this.setHomeSystem(null);
   }
}
