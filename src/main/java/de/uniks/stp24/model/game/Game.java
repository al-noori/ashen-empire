package de.uniks.stp24.model.game;
import java.time.LocalDateTime;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_SIZE = "size";
   public static final String PROPERTY_PLAYERS_COUNT = "playersCount";
   public static final String PROPERTY_STARTED = "started";
   public static final String PROPERTY_SPEED = "speed";
   public static final String PROPERTY_PERIOD = "period";
   public static final String PROPERTY_MAX_PLAYERS = "maxPlayers";
   public static final String PROPERTY_OWNER = "owner";
   public static final String PROPERTY_MEMBERS = "members";
   public static final String PROPERTY_FRACTIONS = "fractions";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String id;
   private String name;
   private int size;
   private int playersCount;
   protected PropertyChangeSupport listeners;
   private boolean started;
   private int speed;
   private int period;
   private int maxPlayers;
   private User owner;
   private List<User> members;
   private List<Fraction> fractions;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Game setCreatedAt(LocalDateTime value)
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

   public Game setUpdatedAt(LocalDateTime value)
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

   public String getId()
   {
      return this.id;
   }

   public Game setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public Game setName(String value)
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

   public int getSize()
   {
      return this.size;
   }

   public Game setSize(int value)
   {
      if (value == this.size)
      {
         return this;
      }

      final int oldValue = this.size;
      this.size = value;
      this.firePropertyChange(PROPERTY_SIZE, oldValue, value);
      return this;
   }

   public int getPlayersCount()
   {
      return this.playersCount;
   }

   public Game setPlayersCount(int value)
   {
      if (value == this.playersCount)
      {
         return this;
      }

      final int oldValue = this.playersCount;
      this.playersCount = value;
      this.firePropertyChange(PROPERTY_PLAYERS_COUNT, oldValue, value);
      return this;
   }

   public boolean isStarted()
   {
      return this.started;
   }

   public Game setStarted(boolean value)
   {
      if (value == this.started)
      {
         return this;
      }

      final boolean oldValue = this.started;
      this.started = value;
      this.firePropertyChange(PROPERTY_STARTED, oldValue, value);
      return this;
   }

   public int getSpeed()
   {
      return this.speed;
   }

   public Game setSpeed(int value)
   {
      if (value == this.speed)
      {
         return this;
      }

      final int oldValue = this.speed;
      this.speed = value;
      this.firePropertyChange(PROPERTY_SPEED, oldValue, value);
      return this;
   }

   public int getPeriod()
   {
      return this.period;
   }

   public Game setPeriod(int value)
   {
      if (value == this.period)
      {
         return this;
      }

      final int oldValue = this.period;
      this.period = value;
      this.firePropertyChange(PROPERTY_PERIOD, oldValue, value);
      return this;
   }

   public int getMaxPlayers()
   {
      return this.maxPlayers;
   }

   public Game setMaxPlayers(int value)
   {
      if (value == this.maxPlayers)
      {
         return this;
      }

      final int oldValue = this.maxPlayers;
      this.maxPlayers = value;
      this.firePropertyChange(PROPERTY_MAX_PLAYERS, oldValue, value);
      return this;
   }

   public User getOwner()
   {
      return this.owner;
   }

   public Game setOwner(User value)
   {
      if (Objects.equals(value, this.owner))
      {
         return this;
      }

      final User oldValue = this.owner;
      this.owner = value;
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
      return this;
   }

   public List<User> getMembers()
   {
      return this.members != null ? Collections.unmodifiableList(this.members) : Collections.emptyList();
   }

   public Game withMembers(User value)
   {
      if (this.members == null)
      {
         this.members = new CopyOnWriteArrayList<>();
      }
      if (!this.members.contains(value))
      {
         this.members.add(value);
         value.setGame(this);
         this.firePropertyChange(PROPERTY_MEMBERS, null, value);
      }
      return this;
   }

   public Game withoutMembers(User value)
   {
      if (this.members != null && this.members.remove(value))
      {
         value.setGame(null);
         this.firePropertyChange(PROPERTY_MEMBERS, value, null);
      }
      return this;
   }

   public Game withoutMembers(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withoutMembers(item);
      }
      return this;
   }

   public List<Fraction> getFractions()
   {
      return this.fractions != null ? Collections.unmodifiableList(this.fractions) : Collections.emptyList();
   }

   public Game withFractions(Fraction value)
   {
      if (this.fractions == null)
      {
         this.fractions = new CopyOnWriteArrayList<>();
      }
      if (!this.fractions.contains(value))
      {
         this.fractions.add(value);
         value.setGame(this);
         this.firePropertyChange(PROPERTY_FRACTIONS, null, value);
      }
      return this;
   }

   public Game withoutFractions(Fraction value)
   {
      if (this.fractions != null && this.fractions.remove(value))
      {
         value.setGame(null);
         this.firePropertyChange(PROPERTY_FRACTIONS, value, null);
      }
      return this;
   }

   public Game withoutFractions(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withoutFractions(item);
      }
      return this;
   }

   public Game withMembers(User... value)
   {
      for (final User item : value)
      {
         this.withMembers(item);
      }
      return this;
   }

   public Game withMembers(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withMembers(item);
      }
      return this;
   }

   public Game withoutMembers(User... value)
   {
      for (final User item : value)
      {
         this.withoutMembers(item);
      }
      return this;
   }

   public Game withFractions(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withFractions(item);
      }
      return this;
   }

   public Game withFractions(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withFractions(item);
      }
      return this;
   }

   public Game withoutFractions(Fraction... value)
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
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutMembers(new ArrayList<>(this.getMembers()));
      this.withoutFractions(new ArrayList<>(this.getFractions()));
   }
}
